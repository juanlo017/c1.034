
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.auditRecords.Mark;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordShowService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		CodeAudit codeAudit;

		auditRecordId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditByAuditRecordId(auditRecordId);
		status = codeAudit != null && (!codeAudit.isDraftMode() || super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord objects;
		int id;

		id = super.getRequest().getData("id", int.class);
		objects = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		SelectChoices choices;

		Dataset dataset;
		choices = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "startTime", "endTime", "mark", "link");
		dataset.put("masterId", object.getCodeAudit().getId());
		dataset.put("marks", choices);
		dataset.put("draftMode", object.getCodeAudit().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
