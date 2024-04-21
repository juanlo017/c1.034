
package acme.features.developer.trainingmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleDeleteService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingModuleId;
		Project project;

		trainingModuleId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectByTrainingModuleId(trainingModuleId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode");
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode");
		dataset.put("masterId", object.getProject().getId());
		dataset.put("draftMode", object.getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
