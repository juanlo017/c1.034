/*
 * AuthenticatedAnnouncementListService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectListMineService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Manager.class));
	}

	@Override
	public void load() {
		Collection<Project> projects;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		projects = this.repository.findManyProjectsByManagerId(principal.getActiveRoleId());

		super.getBuffer().addData(projects);
	}

	@Override
	public void unbind(final Project projects) {
		assert projects != null;

		Dataset dataset;

		dataset = super.unbind(projects, "code", "title", "draftMode");

		super.getResponse().addData(dataset);
	}

}
