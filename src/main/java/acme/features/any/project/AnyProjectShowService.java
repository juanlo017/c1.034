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

package acme.features.any.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;

@Service
public class AnyProjectShowService extends AbstractService<Any, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Project project = this.repository.findOneProjectById(id);

		super.getResponse().setAuthorised(!project.isDraftMode());
	}

	@Override
	public void load() {
		Project project;
		int id;

		id = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(id);

		super.getBuffer().addData(project);
	}

	@Override
	public void unbind(final Project project) {
		assert project != null;

		//CALCULAR PROPIEDADES DERIVADAS EN EL UNBIND
		int cost = 0;

		Dataset dataset;
		//atributos a pasar a la vista
		dataset = super.unbind(project, "code", "title", "abstractText", "fatalErrors", "link", "cost", "draftMode");

		super.getResponse().addData(dataset);
	}

}
