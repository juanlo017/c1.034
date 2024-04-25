
package acme.features.developer.trainingsession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionShowService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperTrainingSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int sessionId;
		TrainingModule module;

		sessionId = super.getRequest().getData("id", int.class);
		module = this.repository.findOneTrainingModuleByTrainingSessionId(sessionId);
		status = module != null && (!module.isDraftMode() || super.getRequest().getPrincipal().hasRole(module.getDeveloper()));

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrainingSession session;
		int id;

		id = super.getRequest().getData("id", int.class);
		session = this.repository.findOneTrainingSessionById(id);

		super.getBuffer().addData(session);
	}

	@Override
	public void unbind(final TrainingSession object) {

		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "timePeriod", "location", "instructor", "email", "link", "draftMode");
		dataset.put("masterId", object.getTrainingModule().getId());

		super.getResponse().addData(dataset);
	}
}
