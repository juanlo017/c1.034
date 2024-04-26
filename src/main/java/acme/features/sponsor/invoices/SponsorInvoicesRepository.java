
package acme.features.sponsor.invoices;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.configuration.Configuration;
import acme.entities.sponsorships.Invoices;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface SponsorInvoicesRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.id = :sponsorshipId")
	Sponsorship findOneSponsorshipById(int sponsorshipId);

	@Query("select i from Invoices i where i.id = :invoiceId")
	Invoices findOneInvoiceById(int invoiceId);

	@Query("select i from Invoices i where i.code = :invoiceCode")
	Invoices findOneInvoiceByCode(String invoiceCode);

	@Query("select i from Invoices i where i.sponsorship.id = :sponsorshipId")
	Collection<Invoices> findAllInvoicesByMasterId(int sponsorshipId);

	@Query("select i.sponsorship from Invoices i where i.id = :invoiceId")
	Sponsorship findOneSponsorshipByInvoiceId(int invoiceId);

	@Query("select c from Configuration c")
	List<Configuration> findConfiguration();

	@Query("select i from Invoices i where i.sponsorship.id = :masterId")
	Collection<Invoices> findManyInvoicesByMasterId(int masterId);

}
