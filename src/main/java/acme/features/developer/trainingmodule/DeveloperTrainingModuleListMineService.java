
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleListMineService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<TrainingModule> trainingModules;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		trainingModules = this.repository.findManyTrainingModulesByDeveloperId(principal.getActiveRoleId());

		super.getBuffer().addData(trainingModules);
	}

	@Override
	public void unbind(final TrainingModule trainingModules) {
		assert trainingModules != null;

		Dataset dataset;

		dataset = super.unbind(trainingModules, "code", "details", "optionalLink", "draftMode");

		super.getResponse().addData(dataset);
	}

}
