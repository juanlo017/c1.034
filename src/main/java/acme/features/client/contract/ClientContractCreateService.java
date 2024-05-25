
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService<Client, Contract> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Contract contract;
		Client client;

		Principal principal = super.getRequest().getPrincipal();
		client = this.repository.findClientById(principal.getActiveRoleId());

		contract = new Contract();
		contract.setClient(client);

		super.getBuffer().addData(contract);
	}

	@Override
	public void bind(final Contract contract) {

		assert contract != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);

		super.bind(contract, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");
		contract.setProject(project);
	}

	@Override
	public void validate(final Contract contract) {
		assert contract != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;

			existing = this.repository.findContractById(contract.getId());
			super.state(existing == null, "code", "client.contrct.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Contract contract) {

		assert contract != null;

		Date now = MomentHelper.getCurrentMoment();

		contract.setDraftMode(false);
		contract.setInstantiationMoment(now);

		this.repository.save(contract);
	}

	@Override
	public void unbind(final Contract contract) {

		assert contract != null;

		Dataset dataset;

		Collection<Project> projects;
		SelectChoices choices;

		projects = this.repository.findAllProjects();
		choices = SelectChoices.from(projects, "code", contract.getProject());

		dataset = super.unbind(contract, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");

		dataset.put("choices", choices);
		dataset.put("instantiationMoment", MomentHelper.getCurrentMoment());

		super.getResponse().addData(dataset);
	}

}
