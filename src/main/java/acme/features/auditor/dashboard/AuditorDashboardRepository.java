
package acme.features.auditor.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuditorDashboardRepository extends AbstractRepository {

	@Query("select count (ca) from CodeAudit ca where ca.type = acme.entities.codeAudits.Type.STATIC and ca.draftMode=false and ca.auditor.id= :id")
	Integer totalNumberOfStaticCodeAudit(int id);

	@Query("select count (ca) from CodeAudit ca where ca.type = acme.entities.codeAudits.Type.DYNAMIC and ca.draftMode=false and ca.auditor.id= :id")
	Integer totalNumberOfDynamicCodeAudit(int id);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :id")
	Double averageNumberOfAuditRecords(int id);
	/*
	 * @Query("select stddev(count(ar) from AuditRecord ar where ar.codeAudit.id =  a.id) from CodeAudit a where a.auditor.id = :id")
	 * Double deviationNumberOfAuditRecords(int id);
	 */
	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :id")
	Integer minimumNumberOfAuditRecords(int id);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = a.id) from CodeAudit a where a.auditor.id = :id")
	Integer maximumNumberOfAuditRecords(int id);

	@Query("select avg(time_to_sec(timediff(ar.endTime,ar.startTime)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double averageTimeOfPeriod(int id);

	@Query("select stddev(time_to_sec(timediff(ar.endTime,ar.startTime)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double deviationTimeOfPeriod(int id);

	@Query("select min(time_to_sec(timediff(ar.endTime,ar.startTime)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double minimumTimeOfPeriod(int id);

	@Query("select max(time_to_sec(timediff(ar.endTime,ar.startTime)) / 3600) from AuditRecord ar where ar.codeAudit.auditor.id = :id")
	Double maximumTimeOfPeriod(int id);

}
