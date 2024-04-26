
package acme.features.sponsor.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.configuration.Configuration;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("select i from Invoices i")
	Collection<Invoices> findAllInvoices();

	@Query("select s from Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int sponsorId);

	@Query("select i from Invoices i where i.sponsorship.sponsor.id = :sponsorId")
	Collection<Invoices> findManyInvoicesBySponsorId(int sponsorId);

	@Query("select s.amount from Sponsorship s where s.sponsor.id = :sponsorId and s.draftMode = false")
	Collection<Money> findManyPublishedAmountsBySponsorId(int sponsorId);

	@Query("select c from Configuration c")
	List<Configuration> findConfiguration();

	@Query("select s from Sponsor s where s.id = :sponsorId")
	Sponsor findSponsorById(int sponsorId);

	@Query("select i.quantity from Invoices i where i.sponsorship.sponsor.id = :sponsorId and i.draftMode = false")
	Collection<Money> findManyPublishedQuantitiesBySponsorId(int sponsorId);
}
