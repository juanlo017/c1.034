
package acme.features.client.progresslog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Controller
public class ClientProgressLogController extends AbstractController<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogCreateService		createService;

	@Autowired
	private ClientProgressLogDeleteService		deleteService;

	@Autowired
	private ClientProgressLogUpdateService		updateService;

	@Autowired
	private ClientProgressLogListMineService	listMineService;

	@Autowired
	private ClientProgressLogShowService		showService;

	@Autowired
	private ClientProgressLogPublishService		publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
