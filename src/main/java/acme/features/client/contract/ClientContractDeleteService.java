
package acme.features.client.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractDeleteService extends AbstractService<Client, Contract> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;

		Client client;
		Contract contract;

		id = super.getRequest().getData("id", int.class);
		contract = this.repository.findContractById(id);
		client = contract == null ? null : contract.getClient();
		status = contract != null && contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		super.bind(object, "code", "providerName", "customerName", "goals", "budget", "project");
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		//TODO
	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "providerName", "customerName", "goals", "budget", "project", "draftMode");

		super.getResponse().addData(dataset);
	}

}
