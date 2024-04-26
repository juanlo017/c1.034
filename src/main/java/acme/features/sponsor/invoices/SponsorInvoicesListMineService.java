
package acme.features.sponsor.invoices;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoicesListMineService extends AbstractService<Sponsor, Invoices> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoicesRepository sir;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sponsorship;

		masterId = super.getRequest().getData("masterId", int.class);
		sponsorship = this.sir.findOneSponsorshipById(masterId);
		status = sponsorship != null && (!sponsorship.isDraftMode() || super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Invoices> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.sir.findManyInvoicesByMasterId(masterId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Invoices object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "quantity");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Invoices> objects) {
		assert objects != null;

		int masterId;
		Sponsorship sponsorship;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		sponsorship = this.sir.findOneSponsorshipById(masterId);
		showCreate = sponsorship.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
