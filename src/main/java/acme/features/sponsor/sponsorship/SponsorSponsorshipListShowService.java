
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.TypeSponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipListShowService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository sr;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sp;
		Sponsor sponsor;

		masterId = super.getRequest().getData("id", int.class);
		sp = this.sr.findOneSponsorshipById(masterId);
		sponsor = sp == null ? null : sp.getSponsor();
		status = super.getRequest().getPrincipal().hasRole(sponsor) || sp != null && !sp.isDraftMode();

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
	public void unbind(final Sponsorship object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices choicesP;
		Dataset dataset;

		Collection<Project> projects;

		choices = SelectChoices.from(TypeSponsorship.class, object.getType());
		projects = this.sr.findAllProjects();
		choicesP = SelectChoices.from(projects, "code", object.getProject());
		dataset = super.unbind(object, "code", "moment", "startTime", "endTime", "amount", "type", "optionalEmail", "optionalLink", "draftMode", "project");
		dataset.put("types", choices);
		dataset.put("project", choicesP.getSelected().getKey());
		dataset.put("projects", choicesP);

		super.getResponse().addData(dataset);
	}
}
