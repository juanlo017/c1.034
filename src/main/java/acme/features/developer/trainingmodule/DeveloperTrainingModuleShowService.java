
package acme.features.developer.trainingmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleShowService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findOneTrainingModuleById(trainingModuleId);
		status = trainingModule != null && super.getRequest().getPrincipal().hasRole(trainingModule.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule trainingModule;
		int id;

		id = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(trainingModule);
	}

	@Override
	public void unbind(final TrainingModule trainingModule) {
		assert trainingModule != null;

		Dataset dataset;
		//Atributos a pasar a la vista
		dataset = super.unbind(trainingModule, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode");

		super.getResponse().addData(dataset);
	}

}
