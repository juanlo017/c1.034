
package acme.features.client.progresslog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogDeleteService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int id;
		Contract contract;
		ProgressLog pl;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractByProgressLogId(id);
		pl = this.repository.findProgressLogById(id);
		boolean hasRole = super.getRequest().getPrincipal().hasRole(contract.getClient());

		status = pl.isDraftMode() && contract != null && hasRole;

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

		int id;

		id = super.getRequest().getData("id", int.class);
		Contract contract = this.repository.findContractByProgressLogId(id);

		super.bind(progressLog, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");
		progressLog.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog progressLog) {

		assert progressLog != null;

	}

	@Override
	public void perform(final ProgressLog progressLog) {
		assert progressLog != null;

		this.repository.delete(progressLog);
	}

	@Override
	public void unbind(final ProgressLog progressLog) {
		assert progressLog != null;

		Dataset dataset;

		dataset = super.unbind(progressLog, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson");

		dataset.put("contractId", progressLog.getContract().getId());
		dataset.put("draftMode", progressLog.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
