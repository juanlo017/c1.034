
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		//TODO
	}

	@Override
	public void load() {
		//TODO
	}

	@Override
	public void unbind(final ProgressLog object) {
		//TODO
	}

	@Override
	public void unbind(final Collection<ProgressLog> objects) {
		//TODO
	}

}
