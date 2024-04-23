
package acme.features.developer.trainingmodule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm where tm.developer.id = :id")
	Collection<TrainingModule> findAllTrainingModulesByDeveloperId(int id);

	@Query("select tm from TrainingModule tm")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select tm from TrainingModule tm where tm.id = :trainingModuleId")
	TrainingModule findOneTrainingModuleById(int trainingModuleId);

	@Query("select p from Project p where p.id= :id")
	Project findOneProjectById(int id);

	@Query("select tm.project from TrainingModule tm where tm.id = :id")
	Project findOneProjectByTrainingModuleId(int id);

	@Query("select tm from TrainingModule tm where tm.code = :code")
	TrainingModule findOneTrainingModuleByCode(String code);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findManyTrainingSessionsByTrainingModuleId(int id);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select d from Developer d where d.id= :id")
	Developer findOneDeveloperById(int id);

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findManyProjectsByDraftMode();
}
