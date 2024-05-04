
package acme.features.client.progresslog;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogListMineService extends AbstractService<Client, ProgressLog> {

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
		Collection<ProgressLog> progressLogs;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		progressLogs = this.repository.findAllProgressLogsByClientId(principal.getActiveRoleId());

		super.getBuffer().addData(progressLogs);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;
		SelectChoices choices;
		Collection<Contract> contracts;

		Dataset dataset;
		/*
		 * contracts = this.repository.findAllContracts();
		 * choices = SelectChoices.from(contracts, "contract", object.getContract());
		 */
		dataset = super.unbind(object, "recordId", "responsiblePerson", "completeness");

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();

			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		/*
		 * dataset.put("contract", choices.getSelected().getKey());
		 * dataset.put("contracts", choices);
		 */
		super.getResponse().addData(dataset);
	}

}
