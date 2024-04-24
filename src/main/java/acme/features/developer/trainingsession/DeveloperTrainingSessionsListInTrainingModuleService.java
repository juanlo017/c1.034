
package acme.features.developer.trainingsession;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionsListInTrainingModuleService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<TrainingSession> objects;

		final int trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);
		objects = this.repository.findAllTrainingSessionsByTrainingModuleId(trainingModuleId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;
		int trainingModuleId;

		Collection<TrainingModule> modules = this.repository.findAllTrainingModules();
		SelectChoices choices = SelectChoices.from(modules, "code", object.getTrainingModule());

		final Dataset dataset = super.unbind(object, "code", "instructor", "email");
		dataset.put("trainingModule", choices.getSelected().getLabel());
		dataset.put("trainingModules", choices);

		if (object.isDraftMode()) {
			final Locale local = super.getRequest().getLocale();
			dataset.put("draftMode", local.equals(Locale.ENGLISH) ? "Yes" : "Sí");
		} else
			dataset.put("draftMode", "No");

		trainingModuleId = super.getRequest().getData("trainingModuleId", int.class);

		super.getResponse().addGlobal("trainingModuleId", trainingModuleId);

		super.getResponse().addData(dataset);
	}
}
