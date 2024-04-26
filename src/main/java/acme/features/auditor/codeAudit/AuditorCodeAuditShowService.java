
package acme.features.auditor.codeAudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudits.CodeAudit;
import acme.entities.codeAudits.Type;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditShowService extends AbstractService<Auditor, CodeAudit> {

	@Autowired
	private AuditorCodeAuditRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit objects;
		int id;

		id = super.getRequest().getData("id", int.class);
		objects = this.repository.findOneCodeAuditById(id);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;
		SelectChoices choices;

		Dataset dataset;

		choices = SelectChoices.from(Type.class, object.getType());

		dataset = super.unbind(object, "code", "executionDate", "type", "actions", "link", "draftMode");
		dataset.put("types", choices);
		dataset.put("draftMode", object.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
