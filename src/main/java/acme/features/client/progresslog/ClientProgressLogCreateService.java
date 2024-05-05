
package acme.features.client.progresslog;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
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
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		int contractId;
		Contract contract;

		contractId = super.getRequest().getData("contract", int.class);
		contract = this.repository.findContractById(contractId);

		super.bind(object, "recordId", "registrationMoment", "responsiblePerson", "completeness", "comment", "draftMode");
		object.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;
		//TODO
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;
		object.setDraftMode(true);
		this.repository.save(object);
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

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		dataset.put("contract", choices.getSelected().getKey());
		dataset.put("contracts", choices);
		super.getResponse().addData(dataset);
	}

}
