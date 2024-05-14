
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractListMineService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Contract> contracts;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		contracts = this.repository.findContractsByClientId(principal.getActiveRoleId());

		super.getBuffer().addData(contracts);
	}

	@Override
	public void unbind(final Contract contract) {
		assert contract != null;

		Dataset dataset;

		dataset = super.unbind(contract, "code", "budget", "project");

		super.getResponse().addData(dataset);
	}

}
