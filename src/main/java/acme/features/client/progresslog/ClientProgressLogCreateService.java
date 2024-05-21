
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
public class ClientProgressLogCreateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		ProgressLog object;

		object = new ProgressLog();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog progressLog) {

		assert progressLog != null;

		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contract", int.class);
		contract = this.repository.findContractById(contractId);

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

		progressLog.setDraftMode(true);
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
