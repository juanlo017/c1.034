
package acme.features.developer.trainingmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);

		//Revisar creación del objeto
		object = new TrainingModule();
		object.setCode("");
		object.setCreationMoment(null);
		object.setDetails("");
		object.setDifficultyLevel(null);
		object.setUpdateMoment(null);
		object.setOptionalLink("");
		object.setTotalTime(0);
		object.setDraftMode(true);
		object.setProject(project);
		object.setDeveloper(object.getDeveloper());

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

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("draftMode", object.getProject().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
