
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditRecords.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordListMineService extends AbstractService<Auditor, AuditRecord> {

	//Internal state ----------------------------------------------------------

	@Autowired
	AuditorAuditRecordRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<AuditRecord> auditRecords;
		Principal principal;
		principal = super.getRequest().getPrincipal();
		auditRecords = this.repository.findAuditRecordsByAuditor(principal.getActiveRoleId());

		super.getBuffer().addData(auditRecords);
	}

	@Override
	public void unbind(final AuditRecord auditRecord) {
		assert auditRecord != null;

		Dataset dataset;

		dataset = super.unbind(auditRecord, "code", "auditPeriod", "mark", "link", "draftMode");
		super.getResponse().addData(dataset);
	}

}
