
package acme.features.auditor.codeAudit;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;
		Date moment;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		moment = MomentHelper.getCurrentMoment();
		object = new CodeAudit();
		object.setCode("");
		object.setActions("");
		object.setDraftMode(true);
		object.setExecutionDate(moment);
		object.setAuditor(auditor);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "executionDate", "type", "actions", "link");

	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			CodeAudit existing;

			existing = this.repository.findOneCodeAuditByCode(object.getCode());
			super.state(existing == null, "code", "auditor.code-audit.form.error.duplicated");
		}
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;
		SelectChoices choices;

		Dataset dataset;

		choices = SelectChoices.from(Type.class, object.getType());

		dataset = super.unbind(object, "code", "executionDate", "type", "actions", "link", "draftMode");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
