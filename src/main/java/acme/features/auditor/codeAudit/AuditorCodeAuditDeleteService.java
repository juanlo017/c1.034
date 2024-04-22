
package acme.features.auditor.codeAudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditDeleteService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

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

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.delete(object);
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
