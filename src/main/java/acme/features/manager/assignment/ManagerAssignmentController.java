/*
 * AdministratorDashboardController.java
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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.Assignment;
import acme.roles.Manager;

@Controller
public class ManagerAssignmentController extends AbstractController<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssignmentShowService		showService;
	@Autowired
	private ManagerAssignmentListMineService	listMineService;
	@Autowired
	private ManagerAssignmentCreateService		createService;
	@Autowired
	private ManagerAssignmentDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
	}

}
