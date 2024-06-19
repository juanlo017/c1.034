
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractDeleteService extends AbstractService<Client, Contract> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int id;
		Contract contract;
		Client client;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(id);
		client = contract == null ? null : contract.getClient();

		int activeClientId = super.getRequest().getPrincipal().getActiveRoleId();
		Client activeClient = this.repository.findClientById(activeClientId);

		boolean activeClientIsContractOwner = contract.getClient() == activeClient;
		boolean hasRole = super.getRequest().getPrincipal().hasRole(client);

		status = contract != null && activeClientIsContractOwner && hasRole;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Contract contract;
		int id;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(id);

		super.getBuffer().addData(contract);
	}

	@Override
	public void bind(final Contract contract) {

		assert contract != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);

		super.bind(contract, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "draftMode");
		contract.setProject(project);
	}

	@Override
	public void validate(final Contract contract) {
		
		assert contract != null;
		
	}

	@Override
	public void perform(final Contract contract) {

		assert contract != null;

		Collection<ProgressLog> progressLogs;
		progressLogs = this.repository.findProgressLogsByContractId(contract.getId());

		this.repository.deleteAll(progressLogs);
		this.repository.delete(contract);
	}

	@Override
	public void unbind(final Contract contract) {

		assert contract != null;

		Dataset dataset;

		Collection<Project> projects;
		SelectChoices choices;

		projects = this.repository.findAllProjects();
		choices = SelectChoices.from(projects, "title", contract.getProject());

		dataset = super.unbind(contract, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "draftMode");
		dataset.put("projects", choices);
		dataset.put("project", choices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
