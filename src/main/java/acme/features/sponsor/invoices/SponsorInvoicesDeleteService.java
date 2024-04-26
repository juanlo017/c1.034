
package acme.features.sponsor.invoices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoicesDeleteService extends AbstractService<Sponsor, Invoices> {

	@Autowired
	private SponsorInvoicesRepository sir;


	@Override
	public void authorise() {
		boolean status;
		int invoiceId;
		Sponsorship sponsorship;

		invoiceId = super.getRequest().getData("id", int.class);
		sponsorship = this.sir.findOneSponsorshipByInvoiceId(invoiceId);
		status = sponsorship != null && sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

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

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "optionalLink");

	}

	@Override
	public void validate(final Invoices object) {
		assert object != null;
	}

	@Override
	public void perform(final Invoices object) {
		assert object != null;

		this.sir.delete(object);
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
