/*
 * EmployerJobDeleteService.java
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

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.trainings.TrainingModule;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Project project;
		Manager manager;

		masterId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(masterId);
		manager = project == null ? null : project.getManager();
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractText", "fatalErrors", "link", "cost");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		Collection<Assignment> assignments;
		Collection<Contract> contracts;
		Collection<Sponsorship> sponsoships;
		Collection<TrainingModule> modules;

		//ASSIGNMENT
		assignments = this.repository.findManyAssignmentsByProjectId(object.getId());
		this.repository.deleteAll(assignments);
		//CONTRACT
		contracts = this.repository.findManyContractsByProjectId(object.getId());
		this.repository.deleteAll(contracts);
		//SPONSORSHIPS
		sponsoships = this.repository.findManySponsorshipsByProjectId(object.getId());
		this.repository.deleteAll(sponsoships);
		//TRAINING MODULE
		modules = this.repository.findManyTrainingModulesByProjectId(object.getId());
		this.repository.deleteAll(modules);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstractText", "fatalErrors", "link", "cost", "draftMode");

		super.getResponse().addData(dataset);
	}

}
