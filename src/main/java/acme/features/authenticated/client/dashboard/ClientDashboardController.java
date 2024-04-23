
package acme.features.authenticated.client.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractController;
import acme.forms.ClientDashboard;
import acme.roles.Client;

public class ClientDashboardController extends AbstractController<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
