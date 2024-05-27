
package acme.features.client.progresslog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogPublishService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int progressLogId;
		ProgressLog progressLog;
		Contract contract;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractByProgressLogId(progressLogId);
		progressLog = this.repository.findProgressLogById(progressLogId);
		boolean hasRole = super.getRequest().getPrincipal().hasRole(contract.getClient());

		status = progressLog.isDraftMode() && contract != null && !contract.isDraftMode() && hasRole;

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

		super.bind(progressLog, "recordId", "registrationMoment", "responsiblePerson", "completeness", "comment", "draftMode");
		progressLog.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog progressLog) {
		assert progressLog != null;
		//TODO
	}

	@Override
	public void perform(final ProgressLog progressLog) {

		assert progressLog != null;

		Date now = MomentHelper.getCurrentMoment();

		progressLog.setDraftMode(false);
		progressLog.setRegistrationMoment(now);

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

		dataset = super.unbind(progressLog, "recordId", "registrationMoment", "responsiblePerson", "completeness", "comment", "draftMode");

		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}

}
