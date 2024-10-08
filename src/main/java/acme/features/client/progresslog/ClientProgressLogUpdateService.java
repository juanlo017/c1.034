
package acme.features.client.progresslog;

import java.util.Collection;
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
		boolean progressLogIsRight = progressLog != null && progressLog.isDraftMode();

		status = activeClientIsProgressLogOwner && hasRole && progressLogIsRight;

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

		super.bind(progressLog, "recordId", "responsiblePerson", "completeness", "comment");
		progressLog.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog progressLog) {

		assert progressLog != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {

			String recordId = progressLog.getRecordId();

			ProgressLog existing;
			existing = this.repository.findProgressLogByRecordId(recordId);

			super.state(existing == null || existing.equals(progressLog), "recordId", "client.progress-log.form.error.duplicated-record-id");
			super.state(Pattern.matches("^PG-[A-Z]{1,2}-[0-9]{4}$", recordId), "code", "client.contract.form.error.illegal-code-pattern");
		}

		if (!super.getBuffer().getErrors().hasErrors("completeness"))
			super.state(progressLog.getCompleteness() != null, "completeness", "client.progress-log.form.error.completeness-required");

		if (!super.getBuffer().getErrors().hasErrors("contract"))
			super.state(progressLog.getContract() != null, "contract", "client.progress-log.form.error.unassigned-contract");

		if (progressLog.getContract() != null) {

			final boolean contractInDraftMode = progressLog.getContract().isDraftMode();
			super.state(!contractInDraftMode, "contract", "client.progress-log.form.error.illegal-contract");
		}

		if (!super.getBuffer().getErrors().hasErrors("registrationMoment"))
			super.state(MomentHelper.isBeforeOrEqual(progressLog.getRegistrationMoment(), MomentHelper.getCurrentMoment()), "registrationMoment", "client.progress-log.form.error.illegal-moment");

		super.state(progressLog.isDraftMode(), "draftMode", "client.progress-log.form.error.illegal-publish");
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
		contracts = this.repository.findAllPublishedContracts();
		choices = SelectChoices.from(contracts, "code", progressLog.getContract());

		dataset = super.unbind(progressLog, "recordId", "responsiblePerson", "completeness", "comment", "draftMode");

		dataset.put("choices", choices);

		super.getResponse().addData(dataset);
	}

}
