
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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

		if (super.getRequest().hasData("contractId", Integer.class))
			progressLogs = this.repository.findAllProgressLogsByContractId(super.getRequest().getData("contractId", Integer.class));

		super.getBuffer().addData(progressLogs);
	}

	@Override
	public void unbind(final ProgressLog progressLog) {
		assert progressLog != null;

		Dataset dataset;

		dataset = super.unbind(progressLog, "recordId", "responsiblePerson", "completeness", "draftMode");

		dataset.put("display-contract", progressLog.getContract().getCode());

		super.getResponse().addData(dataset);
	}

}
