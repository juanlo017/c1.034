
package acme.features.sponsor.dashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDashboardRepository sdr;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();
		Sponsor sponsor = this.sdr.findOneSponsorById(id);
		status = sponsor != null && principal.hasRole(Sponsor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();
		SponsorDashboard sponsorDashboard = new SponsorDashboard();
		Collection<Sponsorship> sponsorships = this.sdr.findAllSponsorshipsBySponsorId(userAccountId);
		Collection<Invoices> invoices = this.sdr.findAllInvoicesBySponsorId(userAccountId);

		sponsorDashboard.setSponsorshipsWithLink(0);
		sponsorDashboard.setAverageSponsorshipsAmount(0);
		sponsorDashboard.setDeviationSponsorshipsAmount(0);
		sponsorDashboard.setMaximumSponsorshipsAmount(0);
		sponsorDashboard.setMinimumSponsorshipsAmount(0);
		sponsorDashboard.setInvoicesTaxLessOrEqual21(0);
		sponsorDashboard.setAverageInvoicesQuantity(0);
		sponsorDashboard.setDeviationInvoicesQuantity(0);
		sponsorDashboard.setMaximumInvoicesQuantity(0);
		sponsorDashboard.setMinimumInvoicesQuantity(0);

		if (!sponsorships.isEmpty()) {
			sponsorDashboard.setSponsorshipsWithLink(this.sdr.findSponsorshipsWithLink(userAccountId));

			sponsorDashboard.setAverageSponsorshipsAmount(this.sdr.findAverageSponsorshipsAmount(userAccountId));
			sponsorDashboard.setDeviationSponsorshipsAmount(this.sdr.findDeviationSponsorshipsAmount(userAccountId));
			sponsorDashboard.setMaximumSponsorshipsAmount(this.sdr.findMaximumSponsorshipsAmount(userAccountId));
			sponsorDashboard.setMinimumSponsorshipsAmount(this.sdr.findMinimumSponsorshipsAmount(userAccountId));
		}

		if (!invoices.isEmpty()) {
			sponsorDashboard.setInvoicesTaxLessOrEqual21(this.sdr.findInvoicesTaxLessOrEqual21(userAccountId));
			sponsorDashboard.setAverageInvoicesQuantity(this.sdr.findAverageInvoicesQuantity(userAccountId));
			sponsorDashboard.setDeviationInvoicesQuantity(this.sdr.findDeviationInvoicesQuantity(userAccountId));
			sponsorDashboard.setMaximumInvoicesQuantity(this.sdr.findMaximumInvoicesQuantity(userAccountId));
			sponsorDashboard.setMinimumInvoicesQuantity(this.sdr.findMinimumInvoicesQuantity(userAccountId));
		}

		super.getBuffer().addData(sponsorDashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "invoicesTaxLessOrEqual21", "sponsorshipsWithLink", "averageSponsorshipsAmount", "deviationSponsorshipsAmount", "minimumSponsorshipsAmount", "maximumSponsorshipsAmount", "averageInvoicesQuantity",
			"deviationInvoicesQuantity", "minimumInvoicesQuantity", "maximumInvoicesQuantity");

		super.getResponse().addData(dataset);
	}

}
