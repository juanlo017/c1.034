
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

		boolean status;
		int id;
		ProgressLog progressLog;
		Client client;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findProgressLogById(id);
		client = progressLog == null ? null : progressLog.getContract().getClient();
		boolean hasRole = super.getRequest().getPrincipal().hasRole(client);

		status = progressLog != null && hasRole;

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
	public void unbind(final ProgressLog progressLog) {

		assert progressLog != null;

		Dataset dataset;

		SelectChoices choices;
		Collection<Contract> contracts;

		contracts = this.repository.findAllPublishedContracts();
		choices = SelectChoices.from(contracts, "code", progressLog.getContract());

		dataset = super.unbind(progressLog, "recordId", "registrationMoment", "responsiblePerson", "completeness", "comment", "draftMode");

		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}
}
