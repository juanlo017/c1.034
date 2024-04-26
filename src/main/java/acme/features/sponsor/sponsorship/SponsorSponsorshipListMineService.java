
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipListMineService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository sr;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Sponsorship> sponsorships;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		sponsorships = this.sr.findAllSponsorshipsBySponsorId(principal.getActiveRoleId());

		super.getBuffer().addData(sponsorships);
	}

	@Override
	public void unbind(final Sponsorship sponsorship) {
		assert sponsorship != null;

		Dataset dataset;

		dataset = super.unbind(sponsorship, "code", "amount", "type", "draftMode");

		super.getResponse().addData(dataset);
	}
}
