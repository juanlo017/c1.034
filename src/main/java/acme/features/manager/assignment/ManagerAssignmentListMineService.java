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

package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Assignment;
import acme.roles.Manager;

@Service
public class ManagerAssignmentListMineService extends AbstractService<Manager, Assignment> {

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
		Principal principal = super.getRequest().getPrincipal();

		Collection<Assignment> assignments = this.repository.findManyAssignmentsByManagerId(principal.getActiveRoleId());

		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final Assignment assignment) {
		assert assignment != null;

		Dataset dataset;

		dataset = super.unbind(assignment, "project", "userStory");
		dataset.put("project", assignment.getProject().getTitle());
		dataset.put("userStory", assignment.getUserStory().getTitle());

		super.getResponse().addData(dataset);
	}

}
