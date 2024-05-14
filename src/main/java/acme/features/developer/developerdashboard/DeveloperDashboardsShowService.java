
package acme.features.developer.developerdashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardsShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		DeveloperDashboard dashboard;
		Integer totalNumberOfTrainingModulesWithUpdateMoment;
		Integer totalNumberOfTrainingSessionsWithLink;
		Double avgTimeOfTrainingModule;
		Integer minTimeOfTrainingModule;
		Integer maxTimeOfTrainingModule;
		Double deviationTimeOfTrainingModule;
		int id;

		id = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfTrainingModulesWithUpdateMoment = this.repository.totalNumberOfTrainingModulesWithUpdateMoment(id);
		totalNumberOfTrainingSessionsWithLink = this.repository.totalNumberOfTrainingSessionsWithLink(id);
		avgTimeOfTrainingModule = this.repository.avgTimeOfTrainingModule(id);
		minTimeOfTrainingModule = this.repository.minTimeOfTrainingModule(id);
		maxTimeOfTrainingModule = this.repository.maxTimeOfTrainingModule(id);
		deviationTimeOfTrainingModule = this.repository.deviationTimeOfTrainingModule(id);

		dashboard = new DeveloperDashboard();
		dashboard.setTotalNumberOfTrainingModulesWithUpdateMoment(totalNumberOfTrainingModulesWithUpdateMoment);
		dashboard.setTotalNumberOfTrainingSessionsWithLink(totalNumberOfTrainingSessionsWithLink);
		dashboard.setAvgTimeOfTrainingModule(avgTimeOfTrainingModule);
		dashboard.setMinTimeOfTrainingModule(minTimeOfTrainingModule);
		dashboard.setMaxTimeOfTrainingModule(maxTimeOfTrainingModule);
		dashboard.setDeviationTimeOfTrainingModule(deviationTimeOfTrainingModule);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "totalNumberOfTrainingModulesWithUpdateMoment", "totalNumberOfTrainingSessionsWithLink", "avgTimeOfTrainingModule", "minTimeOfTrainingModule", "maxTimeOfTrainingModule", "deviationTimeOfTrainingModule");

		super.getResponse().addData(dataset);
	}

}
