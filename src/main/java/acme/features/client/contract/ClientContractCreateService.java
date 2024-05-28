
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

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

		contract.setProject(project);

		Date now = MomentHelper.getCurrentMoment();
		contract.setInstantiationMoment(now);

		super.bind(contract, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");
	}

	@Override
	public void validate(final Contract contract) {

		assert contract != null;

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(contract == null, "code", "client.contract.form.error.duplicated-code");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(0 < contract.getBudget().getAmount(), "budget", "client.contract.form.error.negative-budget");

		if (contract.getProject() != null)
			super.state(contract.getBudget().getCurrency().equals(contract.getProject().getCost().getCurrency()), "budget", "client.contract.form.error.different-currency");

		String acceptedCurrencies = this.repository.findAcceptedCurrencies();
		final boolean supportedCurrency = Stream.of(acceptedCurrencies.split(",")).anyMatch(c -> c.equals(contract.getBudget().getCurrency()));

		super.state(supportedCurrency, "budget", "client.contract.form.error.unsupported-currency");
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
