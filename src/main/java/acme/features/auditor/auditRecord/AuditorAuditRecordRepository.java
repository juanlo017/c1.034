
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select ar from AuditRecord ar")
	Collection<AuditRecord> findAllAuditRecords();

	@Query("select ar from AuditRecord ar where ar.id = :auditRecordId")
	AuditRecord findAuditRecordById(int auditRecordId);

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);

	@Query("select ar from AuditRecord ar where ar.codeAudit = :ca")
	Collection<AuditRecord> findAuditRecordsByCodeAudit(CodeAudit ca);

	@Query("select ar from AuditRecord ar where ar.codeAudit.auditor.id = :auditorId")
	Collection<AuditRecord> findAuditRecordsByAuditor(int auditorId);

}
