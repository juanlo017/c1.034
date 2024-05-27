
package acme.features.developer.trainingmodule;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.trainings.DifficultyLevel;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

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

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "optionalLink", "totalTime");

	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingModule existing;

			existing = this.repository.findOneTrainingModuleByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "developer.training-module.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("updateMoment") && object.getUpdateMoment() != null)
			super.state(MomentHelper.isAfter(object.getUpdateMoment(), object.getCreationMoment()), "updateMoment", "developer.training-module.form.error.update-date-not-valid");

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
		{
			Collection<TrainingSession> sessions;
			int totalSessions;

			sessions = this.repository.findAllTrainingSessionsByTrainingModuleId(object.getId());
			totalSessions = sessions.size();
			super.state(totalSessions >= 1, "*", "developer.training-module.form.error.not-enough-training-sessions");
		}
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;

		object.setDraftMode(false);
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
