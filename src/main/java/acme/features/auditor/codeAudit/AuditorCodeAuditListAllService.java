
package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudits.CodeAudit;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditListAllService extends AbstractService<Auditor, CodeAudit> {

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
		Collection<CodeAudit> codeAudits;

		codeAudits = this.repository.findAllCodeAudits();

		super.getBuffer().addData(codeAudits);
	}

	@Override
	public void unbind(final CodeAudit codeAudits) {
		assert codeAudits != null;

		Dataset dataset;

		dataset = super.unbind(codeAudits, "code", "draftMode", "mark", "executionDate");

		super.getResponse().addData(dataset);
	}

}
