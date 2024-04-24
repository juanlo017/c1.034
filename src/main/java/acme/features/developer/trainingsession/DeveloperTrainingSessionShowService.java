
package acme.features.developer.trainingsession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	//Esto ahora mismo acepta todas las peticiones que lleguen
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrainingSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrainingSessionById(id);

		super.getBuffer().addData(object);
	}
	@Override
	public void unbind(final TrainingSession object) {
		assert object != null;

		Collection<TrainingModule> trainingModules;
		SelectChoices trainingModulesChoices;
		Dataset dataset;

		trainingModules = this.repository.findAllTrainingModules();
		trainingModulesChoices = SelectChoices.from(trainingModules, "code", object.getTrainingModule());

		dataset = super.unbind(object, "code", "timePeriod", "location", "instructor", "email", "link", "draftMode", "trainingModule");
		dataset.put("project", trainingModulesChoices.getSelected().getKey());
		dataset.put("projects", trainingModulesChoices);

		super.getResponse().addData(dataset);
	}
}
