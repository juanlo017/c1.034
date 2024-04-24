
package acme.features.developer.trainingsession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Controller
public class DeveloperTrainingSessionController extends AbstractController<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionListAllService					listService;

	@Autowired
	private DeveloperTrainingSessionsListInTrainingModuleService	listInTrainingModuleService;


	@PostConstruct
	public void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addCustomCommand("list-in-training-module", "list", this.listInTrainingModuleService);
	}
}
