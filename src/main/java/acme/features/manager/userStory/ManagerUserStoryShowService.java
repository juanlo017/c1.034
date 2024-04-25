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

package acme.features.manager.userStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Priority;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryShowService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory userStory;
		int id;

		id = super.getRequest().getData("id", int.class);
		userStory = this.repository.findOneUserStoryById(id);

		super.getBuffer().addData(userStory);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		SelectChoices priorityChoices;
		Dataset dataset;

		priorityChoices = SelectChoices.from(Priority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
		dataset.put("priorities", priorityChoices);
		super.getResponse().addData(dataset);
	}

}
