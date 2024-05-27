
package acme.features.developer.trainingsession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Service
public class DeveloperTrainingSessionUpdateService extends AbstractService<Developer, TrainingSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected DeveloperTrainingSessionRepository repository;

	// AbstractService ---------------------------


	@Override
	public void authorise() {

		boolean status;
		int sessionId;
		TrainingModule module;
		TrainingSession session;

		sessionId = super.getRequest().getData("id", int.class);
		module = this.repository.findOneTrainingModuleByTrainingSessionId(sessionId);
		session = this.repository.findOneTrainingSessionById(sessionId);
		status = module != null && module.isDraftMode() && super.getRequest().getPrincipal().hasRole(module.getDeveloper()) && session.isDraftMode();

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
	public void bind(final TrainingSession object) {
		assert object != null;

		super.bind(object, "code", "timePeriodStart", "timePeriodEnd", "location", "instructor", "email", "link");
	}

	@Override
	public void validate(final TrainingSession object) {
		assert object != null;
		String dateString = "2201/01/01 00:00";
		Date futureMostDate = MomentHelper.parse(dateString, "yyyy/MM/dd HH:mm");
		Date startMaximumDate = MomentHelper.parse("2200/12/24 23:59", "yyyy/MM/dd HH:mm");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			TrainingSession existing;

			existing = this.repository.findOneTrainingSessionByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "developer.training-session.form.error.duplicated");
		}
		if (object.getTimePeriodStart() != null) {
			if (!super.getBuffer().getErrors().hasErrors("timePeriodStart"))
				super.state(MomentHelper.isBefore(object.getTimePeriodStart(), futureMostDate), "timePeriodStart", "developer.training-session.form.error.date-not-before-limit");

			if (!super.getBuffer().getErrors().hasErrors("timePeriodStart"))
				super.state(MomentHelper.isBefore(object.getTimePeriodStart(), startMaximumDate), "timePeriodStart", "developer.training-session.form.error.date-not-before-limit-week");

			if (!super.getBuffer().getErrors().hasErrors("timePeriodStart")) {
				TrainingModule module;
				int id;
				Date minimumStart;
				id = super.getRequest().getData("id", int.class);
				module = this.repository.findOneTrainingModuleByTrainingSessionId(id);
				minimumStart = MomentHelper.deltaFromMoment(module.getCreationMoment(), 7, ChronoUnit.DAYS);

				super.state(MomentHelper.isAfter(object.getTimePeriodStart(), minimumStart), "timePeriodStart", "developer.training-session.form.error.creation-moment-invalid");
			}
			if (object.getTimePeriodEnd() != null) {

				if (!super.getBuffer().getErrors().hasErrors("timePeriodEnd")) {
					Date minimumEnd;

					minimumEnd = MomentHelper.deltaFromMoment(object.getTimePeriodStart(), 7, ChronoUnit.DAYS);
					super.state(object.getTimePeriodStart() != null && MomentHelper.isAfter(object.getTimePeriodEnd(), minimumEnd), "timePeriodEnd", "developer.training-session.form.error.too-close");
				}
				if (!super.getBuffer().getErrors().hasErrors("timePeriodEnd"))
					super.state(MomentHelper.isBefore(object.getTimePeriodEnd(), futureMostDate), "timePeriodEnd", "developer.training-session.form.error.date-not-before-limit");

			}

		}

	}

	@Override
	public void perform(final TrainingSession object) {
		assert object != null;

		this.repository.save(object);
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
