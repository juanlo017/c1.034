
package acme.features.client.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractsCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractsRepository repository;

	// AbstractService<Client, Contract> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		Principal principal;
		int clientId;
		Client client;

		principal = super.getRequest().getPrincipal();
		clientId = principal.getActiveRoleId();
		client = this.repository.findClientById(clientId);

		object = new Contract();
		object.setClient(client);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;

			existing = this.repository.findContractById(object.getId());
			super.state(existing == null, "code", "client.contrct.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		Dataset dataset;

		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");

		super.getResponse().addData(dataset);
	}

	/*
	 * @Override
	 * public void onSuccess() {
	 * if (super.getRequest().getMethod().equals("POST"))
	 * PrincipalHelper.handleUpdate();
	 * }
	 */

}
