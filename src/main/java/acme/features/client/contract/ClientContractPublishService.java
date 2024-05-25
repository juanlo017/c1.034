
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	@Autowired
	private ClientContractRepository repository;


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
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract contract) {
		assert contract != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findProjectById(projectId);
		contract.setProject(project);
		super.bind(contract, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "draftMode");

	}

	@Override
	public void validate(final Contract contract) {
		assert contract != null;

		//TODO
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

		dataset = super.unbind(contract, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "draftMode");

		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}

}
