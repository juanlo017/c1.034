
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
public class ClientProgressLogShowService extends AbstractService<Client, ProgressLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Boolean status;
		int id;
		ProgressLog progressLog;
		Client client;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		client = progressLog == null ? null : progressLog.getContract().getClient();
		status = super.getRequest().getPrincipal().hasRole(client) || progressLog != null && !progressLog.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		ProgressLog progressLog;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;
		SelectChoices choices;
		Collection<Contract> contracts;

		Dataset dataset;
		contracts = this.repository.findAllContracts();
		choices = SelectChoices.from(contracts, "code", object.getContract());
		dataset = super.unbind(object, "recordId", "registrationMoment", "responsiblePerson", "completeness", "comment", "draftMode");

		dataset.put("contract", choices.getSelected().getKey());
		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}
}
