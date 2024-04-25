/*
 * AuthenticatedAnnouncementShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerAssignmentShowService extends AbstractService<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Assignment object = this.repository.findAssignmentById(super.getRequest().getData("id", int.class));
		Manager manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		boolean status = super.getRequest().getPrincipal().hasRole(Manager.class) && object.getProject().getManager().equals(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Assignment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAssignmentById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		Collection<Project> projects = this.repository.findAllProjects();
		Collection<UserStory> userStories = this.repository.findAllUserStories();

		SelectChoices projectChoices = SelectChoices.from(projects, "title", object.getProject());
		SelectChoices userStoriesChoices = SelectChoices.from(userStories, "title", object.getUserStory());

		Dataset dataset;
		dataset = super.unbind(object, "project", "userStory");

		dataset.put("userStory", userStoriesChoices.getSelected().getKey());
		dataset.put("userStoriesChoices", userStoriesChoices);

		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("projectChoices", projectChoices);

		dataset.put("showDelete", object.getProject().isDraftMode());
		super.getResponse().addData(dataset);
	}
}
