
package acme.features.manager.project;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.Project;
import acme.features.manager.project.ManagerProjectListMineService;
import acme.roles.Manager;

@Controller
public class ManagerProjectController extends AbstractController<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectShowService		showService;

	@Autowired
	private ManagerProjectListAllService	listAllService;

	@Autowired
	private ManagerProjectListMineService	listMineService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("list-mine", "list", this.listMineService);
	}

}
