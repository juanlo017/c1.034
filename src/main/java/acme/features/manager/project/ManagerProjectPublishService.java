
package acme.features.manager.project;

import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	@Autowired
	private ManagerProjectRepository repository;


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

		super.bind(object, "code", "title", "abstractText", "fatalErrors", "link", "cost", "draftMode");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		int projectId = super.getRequest().getData("id", int.class);
		Collection<UserStory> stories = this.repository.findAllUserStoriesByProjectId(projectId);

		boolean hasAllStoriesPublished = stories.stream().allMatch(x -> !x.isDraftMode());

		super.state(hasAllStoriesPublished, "*", "manager.project.form.error.notAllStoriesPublished");
		super.state(!object.isFatalErrors(), "*", "manager.project.form.error.hasFatalErrors");
		super.state(!stories.isEmpty(), "*", "manager.project.form.error.numberOfUserStories");
		super.state(object.isDraftMode(), "*", "manager.project.form.error.isPublished");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing = this.repository.findOneProjectByCode(object.getCode());
			if (existing != null)
				super.state(existing.equals(object), "code", "manager.project.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			String acceptedCurrencies = this.repository.findValidCurrencies(object.getCode());
			List<Object> currencies = Arrays.asList(acceptedCurrencies.split(","));
			super.state(currencies.contains(object.getCost().getCurrency()), "cost", "manager.project.form.error.currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost"))
			super.state(object.getCost().getAmount() >= 0., "cost", "manager.project.error.cost.negative-price");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstractText", "fatalErrors", "link", "cost", "draftMode");

		super.getResponse().addData(dataset);
	}
}
