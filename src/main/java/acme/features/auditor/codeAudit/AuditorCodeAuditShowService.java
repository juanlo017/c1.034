
package acme.features.auditor.codeAudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit codeAudit;
		int id;

		id = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findCodeAuditById(id);

		super.getBuffer().addData(codeAudit);
	}

	@Override
	public void unbind(final CodeAudit codeAudit) {
		assert codeAudit != null;

		Dataset dataset;
		//Attributes for the view
		dataset = super.unbind(codeAudit, "code", "executionDate", "type", "actions", "mark", "link", "draftMode");

		super.getResponse().addData(dataset);
	}

}
