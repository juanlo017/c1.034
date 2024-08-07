
package acme.features.developer.trainingmodule;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
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
		boolean status;
		int masterId;

		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();

		masterId = super.getRequest().getData("id", int.class);
		TrainingModule trainingModule = this.repository.findOneTrainingModuleById(masterId);
		status = trainingModule != null && trainingModule.isDraftMode() && trainingModule.getDeveloper().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		TrainingModule object = this.repository.findOneTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;

		int projectId = super.getRequest().getData("project", int.class);
		Project project = this.repository.findOneProjectById(projectId);

		Date currentMoment = MomentHelper.getCurrentMoment();
		Date updateMoment = new Date(currentMoment.getTime() - 1000);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "project");

		object.setUpdateMoment(updateMoment);
		object.setProject(project);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		final String CREATION_MOMENT = "creationMoment";
		final String UPDATE_MOMENT = "updateMoment";

		if (!super.getBuffer().getErrors().hasErrors(CREATION_MOMENT) && !super.getBuffer().getErrors().hasErrors(UPDATE_MOMENT)) {
			final boolean startBeforeEnd = MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment());
			super.state(startBeforeEnd, UPDATE_MOMENT, "developer.training-module.form.error.update-date-not-valid");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final int trainingModuleId = super.getRequest().getData("id", int.class);
			final boolean duplicatedCode = this.repository.findAllTrainingModules().stream().filter(e -> e.getId() != trainingModuleId).anyMatch(e -> e.getCode().equals(object.getCode()));

			super.state(!duplicatedCode, "code", "developer.training-module.form.error.duplicated");
		}

		// Create a Calendar instance
		Calendar minDateCalendar = Calendar.getInstance();
		// Set the date to December 31, 1999, 23:59:00
		minDateCalendar.set(1999, Calendar.DECEMBER, 31, 23, 59, 0);

		// Convert Calendar to Date
		Date minDate = minDateCalendar.getTime();

		// Perform validation
		if (object.getCreationMoment() != null && !super.getBuffer().getErrors().hasErrors("creationMoment"))
			super.state(object.getCreationMoment().after(minDate), "creationMoment", "developer.training-module.form.error.create-date-not-valid");

		String optionalLink = object.getOptionalLink();
		if (optionalLink != null && optionalLink.equals("ftp://"))
			super.state(false, "optionalLink", "developer.training-module.form.error.invalid-link");

	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		SelectChoices choices = SelectChoices.from(DifficultyLevel.class, object.getDifficultyLevel());

		Collection<Project> projects = this.repository.findAllProjects();
		SelectChoices projectsChoices = SelectChoices.from(projects, "code", object.getProject());

		Dataset dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime", "draftMode", "project");

		dataset.put("difficulty", choices);
		dataset.put("project", projectsChoices.getSelected().getKey());
		dataset.put("projects", projectsChoices);

		super.getResponse().addData(dataset);
	}
}
