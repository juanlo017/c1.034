
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

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select ca from CodeAudit ca where ca.auditor.id = :auditorId")
	Collection<CodeAudit> findManyCodeAuditByAuditorId(int auditorId);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findOneAuditorById(int id);

	@Query("select ar from AuditRecord ar where ar.id = :id")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select ca from CodeAudit ca where ca.auditor.id = :auditorId")
	Collection<CodeAudit> findManyAvailableCodeAuditByAuditorId(int auditorId);

	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :codeAuditId")
	Collection<AuditRecord> findManyAuditRecordByCodeAuditId(int codeAuditId);

	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :masterId")
	Collection<AuditRecord> findManyAuditRecordByMasterId(int masterId);

	@Query("select ar from AuditRecord ar where ar.code = :code")
	AuditRecord findOneAuditRecordByCode(String code);

	@Query("select ar.codeAudit from AuditRecord ar where ar.id = :id")
	CodeAudit findOneCodeAuditByAuditRecordId(int id);

}
