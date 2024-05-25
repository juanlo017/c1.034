
package acme.features.developer.trainingsession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
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
		TrainingSession session;
		Developer developer;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneTrainingSessionById(sessionId);
		developer = session == null ? null : session.getTrainingModule().getDeveloper();
		status = session != null && super.getRequest().getPrincipal().hasRole(developer);

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

		dataset = super.unbind(object, "code", "timePeriodStart", "timePeriodEnd", "location", "instructor", "email", "link", "draftMode");
		dataset.put("masterId", object.getTrainingModule().getId());

		super.getResponse().addData(dataset);
	}
}
