
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.configuration.Configuration;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select s from Sponsorship s")
	Collection<Sponsorship> findAllSponsorships();

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findAllSponsorshipsBySponsorId(int sponsorId);

	@Query("select sp from Sponsor sp where sp.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select s from Sponsorship s where s.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

	@Query("select i from Invoices i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoices> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyProjectsByAvailability();

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select s from Sponsorship s where s.draftMode = false")
	Collection<Sponsorship> findManySponsorshipsByAvailability();

	@Query("select i from Invoices i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoices> findAllInvoicesBySponsorshipId(int sponsorshipId);

	@Query("select c from Configuration c")
	List<Configuration> findConfiguration();
}
