
package acme.features.client.contract;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractShowService extends AbstractService<Client, Contract> {

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
	public void unbind(final Contract contract) {

		assert contract != null;

		Dataset dataset;

		SelectChoices choices;
		Collection<Project> projects;

		projects = this.repository.findAllPublishedProjects();
		choices = SelectChoices.from(projects, "code", contract.getProject());

		dataset = super.unbind(contract, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "project", "draftMode");

		dataset.put("choices", choices);

		if (contract.getProject() != null) {

			Project project = contract.getProject();

			List<Contract> contractsOfProject = List.copyOf(this.repository.findContractsByProjectCode(project.getCode()));

			Double sumOfCosts = contractsOfProject.stream().filter(c -> !c.equals(contract)).map(c -> c.getBudget().getAmount()).reduce(.0, (x, y) -> x + y);
			Double remainingAmount = project.getCost().getAmount() - sumOfCosts - contract.getBudget().getAmount();
			String remainingBudget = String.format("%s %s", project.getCost().getCurrency(), remainingAmount);

			dataset.put("remainingBudget", remainingBudget);
		}

		super.getResponse().addData(dataset);
	}

}
