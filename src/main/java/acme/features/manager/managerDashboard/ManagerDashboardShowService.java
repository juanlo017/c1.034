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

package acme.features.manager.managerDashboard;

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
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Manager.class));
	}

	@Override
	public void load() {
		ManagerDashboard dashboard;
		int managerId = super.getRequest().getPrincipal().getActiveRoleId();

		int mustUserStories = this.repository.countMustUserStories(managerId);
		int shouldUserStories = this.repository.countShouldUserStories(managerId);
		int couldUserStories = this.repository.countCouldUserStories(managerId);
		int wontUserStories = this.repository.countWontUserStories(managerId);

		dashboard = new ManagerDashboard();
		dashboard.setMustUserStories(mustUserStories);
		dashboard.setShouldUserStories(shouldUserStories);
		dashboard.setCouldUserStories(couldUserStories);
		dashboard.setWontUserStories(wontUserStories);

		dashboard.setAvgCostOfProjectEUR(this.repository.avgCostOfProject("EUR", managerId));
		dashboard.setAvgCostOfProjectGBP(this.repository.avgCostOfProject("GBP", managerId));
		dashboard.setAvgCostOfProjectUSD(this.repository.avgCostOfProject("USD", managerId));

		dashboard.setMinCostOfProjectEUR(this.repository.minCostOfProject("EUR", managerId));
		dashboard.setMinCostOfProjectGBP(this.repository.minCostOfProject("GBP", managerId));
		dashboard.setMinCostOfProjectUSD(this.repository.minCostOfProject("USD", managerId));

		dashboard.setMaxCostOfProjectEUR(this.repository.maxCostOfProject("EUR", managerId));
		dashboard.setMaxCostOfProjectGBP(this.repository.maxCostOfProject("GBP", managerId));
		dashboard.setMaxCostOfProjectUSD(this.repository.maxCostOfProject("USD", managerId));

		dashboard.setDeviationCostOfProjectEUR(this.repository.deviationCostOfProject("EUR", managerId));
		dashboard.setDeviationCostOfProjectGBP(this.repository.deviationCostOfProject("GBP", managerId));
		dashboard.setDeviationCostOfProjectUSD(this.repository.deviationCostOfProject("USD", managerId));

		///

		dashboard.setAvgCostOfUserStory(this.repository.avgCostOfUserStory(managerId));
		dashboard.setMinCostOfUserStory(this.repository.minCostOfUserStory(managerId));
		dashboard.setMaxCostOfUserStory(this.repository.maxCostOfUserStory(managerId));
		dashboard.setDeviationCostOfUserStory(this.repository.deviationCostOfUserStory(managerId));

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "mustUserStories", "shouldUserStories", "couldUserStories", "wontUserStories", "avgCostOfProjectEUR", "avgCostOfProjectUSD", "avgCostOfProjectGBP", "minCostOfProjectEUR", "minCostOfProjectUSD", "minCostOfProjectGBP",
			"maxCostOfProjectEUR", "maxCostOfProjectUSD", "maxCostOfProjectGBP", "deviationCostOfProjectEUR", "deviationCostOfProjectUSD", "deviationCostOfProjectGBP", "avgCostOfUserStory", "minCostOfUserStory", "maxCostOfUserStory",
			"deviationCostOfUserStory");

		super.getResponse().addData(dataset);
	}

}
