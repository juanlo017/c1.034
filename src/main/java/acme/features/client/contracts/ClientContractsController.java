
package acme.features.client.contracts;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Controller
public class ClientContractsController extends AbstractController<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractsListMineService		listService;

	@Autowired
	private ClientContractsShowService		showService;

	@Autowired
	private ClientContractsCreateService	createService;

	@Autowired
	private ClientContractsPublishService	publishService;

	@Autowired
	private ClientContractsUpdateService	updateService;

	@Autowired
	private ClientContractsDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-mine", "list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
