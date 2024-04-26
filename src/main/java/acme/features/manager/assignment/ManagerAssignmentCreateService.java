/*
 * EmployerJobCreateService.java
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
public class ManagerAssignmentCreateService extends AbstractService<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(super.getRequest().getPrincipal().hasRole(Manager.class));
	}

	@Override
	public void load() {
		Assignment object = new Assignment();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Assignment object) {
		assert object != null;

		int projectId = super.getRequest().getData("project", int.class);
		Project project = this.repository.findProjectById(projectId);
		object.setProject(project);

		int userStoryId = super.getRequest().getData("userStory", int.class);
		UserStory userStory = this.repository.findUserStoryById(userStoryId);
		object.setUserStory(userStory);
	}

	@Override
	public void validate(final Assignment object) {

		if (!super.getBuffer().getErrors().hasErrors("project") && object.getProject() != null)
			super.state(object.getProject().isDraftMode(), "project", "manager.assignment.project.notDraftfMode");

		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(object.getProject() != null, "project", "manager.assignment.project.null");
		if (!super.getBuffer().getErrors().hasErrors("userStory"))
			super.state(object.getUserStory() != null, "userStory", "manager.assignment.user-story.null");
		if (!super.getBuffer().getErrors().hasErrors("project") && object.getProject() != null) {
			int assignmentsWithSameProjectAndStory = this.repository.existsAssignmentWithSameProjectAndUserStory(object.getProject(), object.getUserStory());
			super.state(assignmentsWithSameProjectAndStory == 0, "*", "manager.assignment.project.existSame");
		}

	}

	@Override
	public void perform(final Assignment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		int managerId = super.getRequest().getPrincipal().getActiveRoleId();

		Collection<Project> projects = this.repository.findAllPublishedProjectsByManager(managerId);
		Collection<UserStory> userStories = this.repository.findAllPublishedUserStoriesByManager(managerId);

		SelectChoices projectChoices = SelectChoices.from(projects, "title", object.getProject());
		SelectChoices userStoriesChoices = SelectChoices.from(userStories, "title", object.getUserStory());

		Dataset dataset;
		dataset = super.unbind(object, "project", "userStory");

		dataset.put("userStory", userStoriesChoices.getSelected().getLabel());
		dataset.put("userStoriesChoices", userStoriesChoices);

		dataset.put("project", projectChoices.getSelected().getLabel());
		dataset.put("projectChoices", projectChoices);

		super.getResponse().addData(dataset);
	}

}
