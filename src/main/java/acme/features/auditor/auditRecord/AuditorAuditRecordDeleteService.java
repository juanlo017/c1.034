
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditRecords.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	private AuditorAuditRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		AuditRecord auditRecord;
		Auditor auditor;

		masterId = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findOneAuditRecordById(masterId);
		auditor = auditRecord == null ? null : auditRecord.getCodeAudit().getAuditor();
		status = auditRecord != null && super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "startTime", "endTime", "mark", "link");

	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.delete(object);

	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "startTime", "endTime", "mark", "link");

		super.getResponse().addData(dataset);
	}

}
