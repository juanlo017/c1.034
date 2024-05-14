
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.configuration.Configuration;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.TypeSponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository sr;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sponsorship;
		Sponsor sponsor;

		masterId = super.getRequest().getData("id", int.class);
		sponsorship = this.sr.findOneSponsorshipById(masterId);
		sponsor = sponsorship == null ? null : sponsorship.getSponsor();
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.sr.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.sr.findOneProjectById(projectId);

		super.bind(object, "code", "moment", "startTime", "endTime", "amount", "type", "optionalEmail", "optionalLink", "project");
		object.setProject(project);

	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.sr.findOneSponsorshipByCode(object.getCode());
			if (!object.getCode().equals(existing.getCode()))
				super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startTime"))
			super.state(MomentHelper.isAfter(object.getStartTime(), object.getMoment()), "startTime", "sponsor.sponsorship.form.error.start-time-date-not-valid");

		if (!super.getBuffer().getErrors().hasErrors("endTime")) {
			Date startTime;
			Date endTime;
			boolean minDuration;
			boolean endTimeIsAfterStartTime;

			startTime = object.getStartTime();
			endTime = object.getEndTime();
			minDuration = MomentHelper.isLongEnough(startTime, endTime, 1, ChronoUnit.MONTHS);
			endTimeIsAfterStartTime = MomentHelper.isAfter(endTime, startTime);

			super.state(minDuration && endTimeIsAfterStartTime, "startTime", "sponsor.sponsorship.form.error.duration-not-valid");
		}

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			super.state(object.getAmount().getAmount() > 0, "amount", "sponsor.sponsorship.form.error.amount-must-be-positive");
			List<Configuration> conf = this.sr.findConfiguration();
			final boolean foundCurrency = Stream.of(conf.get(0).acceptedCurrencies.split(",")).anyMatch(c -> c.equals(object.getAmount().getCurrency()));

			super.state(foundCurrency, "amount", "sponsor.sponsorship.form.error.currency-not-supported");
		}
		{
			Collection<Invoices> invoices;
			double sponsorshipAmount;
			double invoicesTotalAmount;
			boolean allInvoicesPublished;

			invoices = this.sr.findAllInvoicesBySponsorshipId(object.getId());
			allInvoicesPublished = invoices.stream().filter(i -> i.isDraftMode() == false).count() == invoices.size();
			if (!allInvoicesPublished)
				super.state(allInvoicesPublished, "*", "sponsor.sponsorship.form.error.sponsorship-invoices-must-be-published");

			sponsorshipAmount = object.getAmount().getAmount();
			invoicesTotalAmount = invoices.stream().mapToDouble(i -> i.totalAmount()).sum();

			super.state(sponsorshipAmount == invoicesTotalAmount, "*", "sponsor.sponsorship.form.error.sponsorship-amount-and-invoices-total-amount-must-be-equal");
		}
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setDraftMode(false);
		this.sr.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projectsChoices;
		Collection<Project> projects;

		Dataset dataset;
		choices = SelectChoices.from(TypeSponsorship.class, object.getType());
		projects = this.sr.findAllProjects();
		projectsChoices = SelectChoices.from(projects, "code", object.getProject());
		dataset = super.unbind(object, "code", "moment", "startTime", "endTime", "amount", "type", "optionalEmail", "optionalLink", "draftMode", "project");

		dataset.put("types", choices);
		dataset.put("project", projectsChoices.getSelected().getKey());
		dataset.put("projects", projectsChoices);
		super.getResponse().addData(dataset);

	}
}
