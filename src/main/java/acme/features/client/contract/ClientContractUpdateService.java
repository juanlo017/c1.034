
package acme.features.client.contract;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractUpdateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

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
		boolean contractIsRight = contract != null && contract.isDraftMode();

		status = activeClientIsContractOwner && hasRole && contractIsRight;

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

		super.bind(contract, "code", "providerName", "customerName", "goals", "budget", "project");

		contract.setProject(project);
	}

	@Override
	public void validate(final Contract contract) {

		assert contract != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {

			String contractCode = contract.getCode();

			Contract existing;
			existing = this.repository.findContractByCode(contractCode);

			super.state(existing == null || existing.equals(contract), "code", "client.contract.form.error.duplicated-code");
			super.state(Pattern.matches("^[A-Z]{1,3}-[0-9]{3}$", contractCode), "code", "client.contract.form.error.illegal-code-pattern");
		}

		if (!super.getBuffer().getErrors().hasErrors("budget")) {

			super.state(0 <= contract.getBudget().getAmount(), "budget", "client.contract.form.error.negative-budget");

			String acceptedCurrencies = this.repository.findAcceptedCurrencies();
			final boolean supportedCurrency = Stream.of(acceptedCurrencies.split(",")).anyMatch(c -> c.equals(contract.getBudget().getCurrency()));

			super.state(supportedCurrency, "budget", "client.contract.form.error.unsupported-currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(contract.getProject() != null, "project", "client.contract.form.error.unassigned-project");

		if (contract.getProject() != null) {

			Money projectCost = contract.getProject().getCost();
			List<Contract> contractsOfProject = List.copyOf(this.repository.findContractsByProjectCode(contract.getProject().getCode()));

			Double spentBudget = contractsOfProject.stream().filter(c -> !c.equals(contract)).map(c -> c.getBudget().getAmount()).reduce(.0, (x, y) -> x + y);
			spentBudget += contract.getBudget().getAmount();

			double remainingBudget = projectCost.getAmount() - spentBudget;

			super.state(contract.getBudget().getCurrency().equals(projectCost.getCurrency()), "budget", "client.contract.form.error.different-currency");
			super.state(0 <= remainingBudget, "budget", "client.contract.form.error.budget-greater-than-cost");

			if (!super.getBuffer().getErrors().hasErrors("instantiationMoment"))
				super.state(MomentHelper.isBeforeOrEqual(contract.getInstantiationMoment(), MomentHelper.getCurrentMoment()), "instantiationMoment", "client.contract.form.error.illegal-moment");

			if (!contract.isDraftMode())
				super.state(contract.isDraftMode(), "draftMode", "client.contract.form.error.illegal-publish");

			boolean projectInDraftMode = contract.getProject().isDraftMode();

			if (!projectInDraftMode)
				super.state(projectInDraftMode, "project", "client.contract.form.error.illegal-project");
		}

	}

	@Override
	public void perform(final Contract contract) {

		assert contract != null;

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

		super.getResponse().addData(dataset);
	}
}
