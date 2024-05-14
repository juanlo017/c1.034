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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		int projectId = super.getRequest().getData("id", int.class);
		Project project = this.repository.findOneProjectById(projectId);

		Manager manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());
		boolean status = super.getRequest().getPrincipal().hasRole(Manager.class) && project.getManager().equals(manager);

		super.getResponse().setAuthorised(status);
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

		int numAssignments = this.repository.countAssignmentsByProjectId(object.getId());
		int numContracts = this.repository.countContractsByContractId(object.getId());
		int numRisks = this.repository.countRisksByProjectId(object.getId());
		int numObjectives = this.repository.countObjectivesByProjectId(object.getId());
		int numSponsorships = this.repository.countSponsorshipsByProjectId(object.getId());
		int numTrainingModules = this.repository.countTrainingModuleId(object.getId());

		super.state(numAssignments == 0, "*", "manager.project.delete.exists-assignment");
		super.state(numContracts == 0, "*", "manager.project.delete.exists-contract");
		super.state(numRisks == 0, "*", "manager.project.delete.exists-risk");
		super.state(numObjectives == 0, "*", "manager.project.delete.exists-objective");
		super.state(numSponsorships == 0, "*", "manager.project.delete.exists-sponsorship");
		super.state(numTrainingModules == 0, "*", "manager.project.delete.exists-training-module");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

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
