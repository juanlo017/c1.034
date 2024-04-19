
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleListAllService extends AbstractService<Developer, TrainingModule> {

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

		trainingModules = this.repository.findAllTrainingModules();

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
