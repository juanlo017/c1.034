
package acme.features.sponsor.invoices;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.configuration.Configuration;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoicesUpdateService extends AbstractService<Sponsor, Invoices> {

	@Autowired
	private SponsorInvoicesRepository sir;


	@Override
	public void authorise() {

		boolean status;
		int invoiceId;
		Sponsorship sponsorship;
		Invoices invoice;

		invoiceId = super.getRequest().getData("id", int.class);
		sponsorship = this.sir.findOneSponsorshipByInvoiceId(invoiceId);
		invoice = this.sir.findOneInvoiceById(invoiceId);
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor()) && invoice.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoices object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.sir.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoices object) {
		assert object != null;

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "optionalLink", "project");
	}

	@Override
	public void validate(final Invoices object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoices existing;

			existing = this.sir.findOneInvoiceByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "sponsor.invoice.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("dueDate")) {
			Date registrationTime;
			Date dueDate;
			boolean minDuration;
			boolean dueDateIsAfterRegistrationTime;

			registrationTime = object.getRegistrationTime();
			dueDate = object.getDueDate();
			minDuration = MomentHelper.isLongEnough(registrationTime, dueDate, 1, ChronoUnit.MONTHS);
			dueDateIsAfterRegistrationTime = MomentHelper.isAfter(dueDate, registrationTime);

			super.state(minDuration && dueDateIsAfterRegistrationTime, "dueDate", "sponsor.invoice.form.error.due-date-not-valid");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			super.state(object.getQuantity().getAmount() > 0, "quantity", "sponsor.invoice.form.error.quantity-must-be-positive");
			List<Configuration> conf = this.sir.findConfiguration();
			final boolean foundCurrency = Stream.of(conf.get(0).acceptedCurrencies.split(",")).anyMatch(c -> c.equals(object.getQuantity().getCurrency()));

			super.state(foundCurrency, "quantity", "sponsor.invoice.form.error.currency-not-supported");
		}
	}

	@Override
	public void perform(final Invoices object) {
		assert object != null;

		this.sir.save(object);
	}

	@Override
	public void unbind(final Invoices object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "optionalLink", "draftMode");
		dataset.put("masterId", object.getSponsorship().getId());

		super.getResponse().addData(dataset);

	}
}
