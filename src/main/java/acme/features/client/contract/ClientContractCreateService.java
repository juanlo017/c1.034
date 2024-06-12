
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.datatypes.Money;
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

		contract.setProject(project);

		Date now = MomentHelper.getCurrentMoment();
		contract.setInstantiationMoment(now);

		super.bind(contract, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");
	}

	@Override
	public void validate(final Contract contract) {

		assert contract != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {

			Contract existing;
			existing = this.repository.findContractById(contract.getId());

			super.state(existing == null, "code", "client.contract.form.error.duplicated-code");
			super.state(Pattern.matches("^[A-Z]{1,3}-[0-9]{3}$", contract.getCode()), "code", "client.contract.form.error.illegal-code-pattern");
		}

		if (!super.getBuffer().getErrors().hasErrors("providerName"))
			super.state(contract.getProviderName().isBlank(), "providerName", "client.contract.form.error.blank-field");

		if (!super.getBuffer().getErrors().hasErrors("customerName"))
			super.state(contract.getCustomerName().isBlank(), "customerName", "client.contract.form.error.blank-field");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(contract.getGoals().isBlank(), "goals", "client.contract.form.error.blank-field");

		if (!super.getBuffer().getErrors().hasErrors("budget")) {

			super.state(contract.getBudget().getAmount() < 0, "budget", "client.contract.form.error.negative-budget");

			String acceptedCurrencies = this.repository.findAcceptedCurrencies();
			final boolean supportedCurrency = Stream.of(acceptedCurrencies.split(",")).anyMatch(c -> c.equals(contract.getBudget().getCurrency()));

			super.state(supportedCurrency, "budget", "client.contract.form.error.unsupported-currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(contract.getProject() == null, "goals", "client.contract.form.error.unassigned-project");

		if (contract.getProject() != null) {

			Money projectCost = contract.getProject().getCost();

			super.state(contract.getBudget().getCurrency().equals(projectCost.getCurrency()), "budget", "client.contract.form.error.different-currency");
			super.state(projectCost.getAmount() < contract.getBudget().getAmount(), "budget", "client.contract.form.error.budget-greater-than-cost");
		}
	}

	@Override
	public void perform(final Contract contract) {

		assert contract != null;

		contract.setDraftMode(true);

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
		dataset.put("instantiationMoment", contract.getInstantiationMoment());

		super.getResponse().addData(dataset);
	}

}
