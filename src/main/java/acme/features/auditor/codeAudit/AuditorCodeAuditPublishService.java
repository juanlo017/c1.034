
package acme.features.auditor.codeAudit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.codeAudits.CodeAudit;
import acme.features.auditor.auditRecord.AuditorAuditRecordRepository;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditPublishService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository		repository;

	@Autowired
	private AuditorAuditRecordRepository	auditRecordRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit ca;
		Auditor auditor;

		codeAuditId = super.getRequest().getData("id", int.class);
		ca = this.repository.findCodeAuditById(codeAuditId);
		auditor = ca == null ? null : ca.getAuditor();
		status = ca != null && ca.isDraftMode() && super.getRequest().getPrincipal().hasRole(auditor) && ca.getMark().compareTo("C") >= 0;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit ca;
		int id;

		id = super.getRequest().getData("id", int.class);
		ca = this.repository.findCodeAuditById(id);

		super.getBuffer().addData(ca);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		int auditorId;
		Auditor auditor;

		// Assuming you have a method to retrieve data from the request
		auditorId = super.getRequest().getData("auditor", int.class);
		auditor = this.repository.findOneAuditorById(auditorId);

		// Assuming you have a method to bind specific attributes
		super.bind(object, "code", "executionDate", "type", "actions", "mark", "link", "draftMode");

		// Set the auditor
		object.setAuditor(auditor);
	}

	public String computeModeMark(final Collection<AuditRecord> auditRecords) {

		String mark;
		if (auditRecords == null || auditRecords.isEmpty())
			mark = null; // No audit records, mark cannot be computed

		// Count occurrences of each mark
		Map<String, Integer> markCount = new HashMap<>();
		for (AuditRecord ar : auditRecords) {
			String m = ar.getMark().toString();
			markCount.put(m, markCount.getOrDefault(m, 0) + 1);
		}

		// Find the mark with the highest count (mode)
		String modeMark = null;
		int maxCount = 0;
		for (Map.Entry<String, Integer> entry : markCount.entrySet())
			if (entry.getValue() > maxCount) {
				modeMark = entry.getKey();
				maxCount = entry.getValue();
			}

		mark = modeMark;
		return mark;
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("executionDate")) {
			// Check if execution date is in the past
			// Convert executionDate to LocalDate
			LocalDate executionLocalDate = object.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			// Check if execution date is in the past
			LocalDate currentDate = LocalDate.now(); // Current date
			super.state(executionLocalDate.isBefore(currentDate), "executionDate", "validation.codeaudit.executiondate");

		}

		// Check if mark is valid (at least "C")
		Collection<AuditRecord> auditRecords = this.auditRecordRepo.findAuditRecordsByCodeAudit(object);
		String mark = this.computeModeMark(auditRecords);
		super.state(mark != null && object.getMark().compareTo("C") >= 0, "mark", "validation.codeaudit.mark");

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		int auditorId;
		Auditor auditor;
		Dataset dataset;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		auditor = this.repository.findOneAuditorById(auditorId);

		dataset = super.unbind(object, "code", "executionDate", "type", "actions", "mark", "link", "draftMode");
		dataset.put("auditor", auditor);

		super.getResponse().addData(dataset);
	}

}
