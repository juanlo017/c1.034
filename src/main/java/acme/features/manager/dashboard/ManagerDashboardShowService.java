/*
 * AdministratorDashboardShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ManagerDashboard dashboard;
		int managerId = super.getRequest().getPrincipal().getActiveRoleId();

		int mustUserStories = this.repository.countMustUserStories(managerId);
		int shouldUserStories = this.repository.countShouldUserStories(managerId);
		int couldUserStories = this.repository.countCouldUserStories(managerId);
		int wontUserStories = this.repository.countWontUserStories(managerId);

		double avgCostOfUserStory = this.repository.avgCostOfUserStory(managerId);
		int minCostOfUserStory = this.repository.minCostOfUserStory(managerId);
		int maxCostOfUserStory = this.repository.maxCostOfUserStory(managerId);
		double deviationCostOfUserStory = this.repository.deviationCostOfUserStory(managerId);

		double avgCostOfProject = this.repository.avgCostOfProject(managerId);
		int minCostOfProject = this.repository.minCostOfProject(managerId);
		int maxCostOfProject = this.repository.maxCostOfProject(managerId);
		double deviationCostOfProject = this.repository.deviationCostOfProject(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setMustUserStories(mustUserStories);
		dashboard.setShouldUserStories(shouldUserStories);
		dashboard.setCouldUserStories(couldUserStories);
		dashboard.setWontUserStories(wontUserStories);

		dashboard.setAvgCostOfUserStory(avgCostOfUserStory);
		dashboard.setMinCostOfUserStory(minCostOfUserStory);
		dashboard.setMaxCostOfUserStory(maxCostOfUserStory);
		dashboard.setDeviationCostOfUserStory(deviationCostOfUserStory);

		dashboard.setAvgCostOfProject(avgCostOfProject);
		dashboard.setMinCostOfProject(minCostOfProject);
		dashboard.setMaxCostOfProject(maxCostOfProject);
		dashboard.setDeviationCostOfProject(deviationCostOfProject);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "mustUserStories", "shouldUserStories", "couldUserStories", "wontUserStories", "avgCostOfUserStory", "minCostOfUserStory", "maxCostOfUserStory", "deviationCostOfUserStory", "avgCostOfProject", "minCostOfProject",
			"maxCostOfProject", "deviationCostOfProject");

		super.getResponse().addData(dataset);
	}

}
