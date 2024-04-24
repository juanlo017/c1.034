
package acme.features.manager.project;

import java.util.Collection;

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
		boolean status;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = project == null ? null : project.getManager();

		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

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

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			int projectId = super.getRequest().getData("id", int.class);
			Collection<UserStory> stories = this.repository.findAllUserStoriesByProjectId(projectId);

			boolean hasAllStoriesPublished = stories.stream().allMatch(x -> !x.isDraftMode());

			super.state(hasAllStoriesPublished, "code", "manager.project.form.error.notAllStoriesPublished");
			super.state(!object.isFatalErrors(), "code", "manager.project.form.error.hasFatalErrors");
			super.state(!stories.isEmpty(), "code", "manager.project.form.error.numberOfUserStories");
		}
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
