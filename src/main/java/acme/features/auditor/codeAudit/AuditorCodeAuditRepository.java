
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Repository
public interface AuditorCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca where ca.auditor.id = :auditorId")
	Collection<CodeAudit> findManycodeAuditsByAuditorId(int auditorId);

	@Query("select ca from CodeAudit ca")
	Collection<CodeAudit> findAllCodeAudits();

	@Query("select ca from CodeAudit ca where ca.id = :codeAuditId")
	CodeAudit findCodeAuditById(int codeAuditId);

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findOneAuditorById(int auditorId);

	/*
	 * @Query("select ca.mark from CodeAudit ca where ca.id = :codeAuditId")
	 * String findMarkByCodeAuditId(int codeAuditId);
	 */

}
