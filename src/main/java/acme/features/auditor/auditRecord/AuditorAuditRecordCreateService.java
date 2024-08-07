
package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.auditRecords.Mark;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCreateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AuditRecord object;

		int masterId;
		CodeAudit codeAudit;
		masterId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(masterId);

		object = new AuditRecord();
		object.setCode("");
		object.setCodeAudit(codeAudit);
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
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord existing;

			existing = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(existing == null, "code", "auditor.audit-record.form.error.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("endTime")) {
			Date minimumEnd;

			minimumEnd = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.HOURS);
			super.state(MomentHelper.isBefore(object.getEndTime(), minimumEnd), "endTime", "auditor.audit-record.form.error.too-close");
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "startTime", "endTime", "mark", "link");
		dataset.put("marks", choices);
		//dataset.put("id", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
