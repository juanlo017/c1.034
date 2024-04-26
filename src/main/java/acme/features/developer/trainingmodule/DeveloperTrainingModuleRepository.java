
package acme.features.developer.trainingmodule;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Project;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;
import acme.roles.Developer;

@Repository
public interface DeveloperTrainingModuleRepository extends AbstractRepository {

	@Query("select tm from TrainingModule tm")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select tm from TrainingModule tm where tm.developer.id = :developerId")
	List<TrainingModule> findAllTrainigModulesByDeveloperId(int developerId);

	@Query("select tm.details from TrainingModule tm where tm.developer.id = :developerId")
	List<String> findAllTrainingModulesDetailsByDeveloperId(int developerId);

	@Query("select tm from TrainingModule tm where tm.id = :tmId")
	TrainingModule findOneTrainingModuleById(int tmId);

	@Query("select d from Developer d where d.id = :developerId")
	Developer findOneDeveloperById(int developerId);

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select tm from TrainingModule tm where tm.code = :tmCode")
	TrainingModule findOneTrainingModuleByCode(String tmCode);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :tmId")
	Collection<TrainingSession> findAllTrainingSessionsByTrainingModuleId(int tmId);

}
