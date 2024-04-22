
package acme.features.auditor.auditRecord;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditRecords.AuditRecord;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordPublishService extends AbstractService<Auditor, AuditRecord> {

	//Internal state ----------------------------------------------------------

	@Autowired
	AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		AuditRecord ar;
		Auditor auditor;

		auditRecordId = super.getRequest().getData("id", int.class);
		ar = this.repository.findAuditRecordById(auditRecordId);
		auditor = ar == null ? null : ar.getCodeAudit().getAuditor();
		status = ar != null && super.getRequest().getPrincipal().hasRole(auditor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord ar;
		int id;

		id = super.getRequest().getData("id", int.class);
		ar = this.repository.findAuditRecordById(id);

		super.getBuffer().addData(ar);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		int auditorId;
		Auditor auditor;

		// Assuming you have a method to retrieve data from the request
		auditorId = super.getRequest().getData("auditor", int.class);
		auditor = this.repository.findOneAuditorById(auditorId);

		// Assuming you have a method to bind specific attributes
		super.bind(object, "code", "auditPeriod", "mark", "link", "draftMode");

		// Set the auditor
		object.getCodeAudit().setAuditor(auditor);
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("auditPeriod")) {
			// Check if execution date is in the past
			// Convert executionDate to LocalDate
			LocalDate startTime = object.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endTime = object.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// Check if execution date is in the past
			LocalDate currentDate = LocalDate.now(); // Current date
			super.state(startTime.isBefore(currentDate) && endTime.isBefore(currentDate) && object.getAuditPeriod() > 3600, "executionDate", "validation.auditrecord.auditperiod");

		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		int auditorId;
		Auditor auditor;
		Dataset dataset;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		auditor = this.repository.findOneAuditorById(auditorId);

		dataset = super.unbind(object, "code", "auditPeriod", "mark", "link", "draftMode");
		dataset.put("auditor", auditor);

		super.getResponse().addData(dataset);
	}
}
