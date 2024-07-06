
package acme.features.client.progresslog;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

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

		ProgressLog progressLog;

		progressLog = new ProgressLog();
		progressLog.setDraftMode(true);

		super.getBuffer().addData(progressLog);
	}

	@Override
	public void bind(final ProgressLog progressLog) {

		assert progressLog != null;

		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contract", int.class);
		contract = this.repository.findContractById(contractId);

		Date now = MomentHelper.getCurrentMoment();

		progressLog.setContract(contract);
		progressLog.setRegistrationMoment(now);

		super.bind(progressLog, "recordId", "responsiblePerson", "completeness", "comment", "contract");
	}

	@Override
	public void validate(final ProgressLog progressLog) {

		assert progressLog != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {

			String recordId = progressLog.getRecordId();

			ProgressLog existing;
			existing = this.repository.findProgressLogByRecordId(recordId);

			super.state(existing == null, "recordId", "client.progress-log.form.error.duplicated-record-id");
			super.state(Pattern.matches("^PG-[A-Z]{1,2}-[0-9]{4}$", recordId), "code", "client.contract.form.error.illegal-record-id-pattern");
		}

		if (!super.getBuffer().getErrors().hasErrors("completeness"))
			super.state(progressLog.getCompleteness() != null, "completeness", "client.progress-log.form.error.completeness-required");

		if (!super.getBuffer().getErrors().hasErrors("contract"))
			super.state(progressLog.getContract() != null, "contract", "client.progress-log.form.error.unassigned-contract");

		if (!super.getBuffer().getErrors().hasErrors("registrationMoment"))
			super.state(MomentHelper.isBeforeOrEqual(progressLog.getRegistrationMoment(), MomentHelper.getCurrentMoment()), "registrationMoment", "client.progress-log.form.error.illegal-moment");

		if (!progressLog.isDraftMode())
			super.state(progressLog.isDraftMode(), "draftMode", "client.progress-log.form.error.illegal-publish");

	}

	@Override
	public void perform(final ProgressLog progressLog) {

		assert progressLog != null;
		progressLog.setDraftMode(true);

		this.repository.save(progressLog);
	}

	@Override
	public void unbind(final ProgressLog progressLog) {

		assert progressLog != null;

		Dataset dataset;

		SelectChoices choices;
		Collection<Contract> contracts;

		contracts = this.repository.findAllContracts();
		choices = SelectChoices.from(contracts, "code", progressLog.getContract());

		dataset = super.unbind(progressLog, "recordId", "responsiblePerson", "completeness", "comment", "contract", "draftMode");

		dataset.put("choices", choices);
		dataset.put("registrationMoment", progressLog.getRegistrationMoment());

		super.getResponse().addData(dataset);
	}

}
