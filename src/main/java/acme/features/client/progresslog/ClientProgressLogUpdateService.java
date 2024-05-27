
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogUpdateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int id;
		ProgressLog progressLog;
		Client client;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		client = progressLog == null ? null : progressLog.getContract().getClient();

		int activeClientId = super.getRequest().getPrincipal().getActiveRoleId();
		Client activeClient = this.repository.findClientById(activeClientId);

		boolean activeClientIsProgressLogOwner = progressLog.getContract().getClient() == activeClient;
		boolean hasRole = super.getRequest().getPrincipal().hasRole(client);

		status = progressLog != null && activeClientIsProgressLogOwner && hasRole;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		ProgressLog progressLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void bind(final ProgressLog progressLog) {

		assert progressLog != null;

		Contract contract = progressLog.getContract();

		super.bind(progressLog, "recordId", "responsiblePerson", "completeness", "comment", "draftMode");
		progressLog.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog progressLog) {
		//TODO
	}

	@Override
	public void perform(final ProgressLog progressLog) {

		assert progressLog != null;

		this.repository.save(progressLog);
	}

	@Override
	public void unbind(final ProgressLog progressLog) {

		assert progressLog != null;

		SelectChoices choices;
		Collection<Contract> contracts;

		Dataset dataset;
		contracts = this.repository.findAllContracts();
		choices = SelectChoices.from(contracts, "code", progressLog.getContract());

		dataset = super.unbind(progressLog, "recordId", "responsiblePerson", "completeness", "comment", "draftMode");

		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}

}
