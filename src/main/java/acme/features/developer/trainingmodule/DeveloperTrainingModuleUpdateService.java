
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.trainings.DifficultyLevel;
import acme.entities.trainings.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleUpdateService extends AbstractService<Developer, TrainingModule> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Boolean status;
		int masterId;
		TrainingModule tm;
		Developer developer;

		masterId = super.getRequest().getData("id", int.class);
		tm = this.repository.findOneTrainingModuleById(masterId);
		developer = tm == null ? null : tm.getDeveloper();
		status = tm != null && tm.isDraftMode() && super.getRequest().getPrincipal().hasRole(developer);

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

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "project");
		object.setUpdateMoment(MomentHelper.getCurrentMoment());
		object.setProject(project);

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findOneTrainingModuleByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "developer.training-module.form.error.duplicated");
		}

		if (object.getUpdateMoment() != null && !super.getBuffer().getErrors().hasErrors("updateMoment"))
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.update-date-not-valid");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;
		SelectChoices choices;
		SelectChoices projectsChoices;
		Collection<Project> projects;

		Dataset dataset;
		choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());
		projects = this.repository.findAllProjects();
		projectsChoices = SelectChoices.from(projects, "code", object.getProject());
		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode", "project");
		dataset.put("difficulty", choices);
		dataset.put("project", projectsChoices.getSelected().getKey());
		dataset.put("projects", projectsChoices);
		super.getResponse().addData(dataset);

	}
}
