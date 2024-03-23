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

package acme.features.manager.userStory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryListService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);
		status = project != null && (!project.isDraftMode() || super.getRequest().getPrincipal().hasRole(project.getManager()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<UserStory> stories;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		stories = this.repository.findManyUserStoriesByProjectId(masterId);

		super.getBuffer().addData(stories);
	}

	@Override
	public void unbind(final UserStory story) {
		assert story != null;

		Dataset dataset;

		dataset = super.unbind(story, "title", "description", "estimatedCost");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<UserStory> stories) {
		assert stories != null;

		int masterId;
		Project project;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		project = this.repository.findOneProjectById(masterId);
		//Si proyecto esta en draftMode y es manager se pueden editar/crear/borrar etc...
		showCreate = project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
