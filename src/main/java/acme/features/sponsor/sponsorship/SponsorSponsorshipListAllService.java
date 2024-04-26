
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipListAllService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository sponsorshipRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Sponsorship> sponsorships;

		sponsorships = this.sponsorshipRepository.findManySponsorshipsByAvailability();

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
