
package acme.features.developer.trainingsession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trainings.TrainingModule;
import acme.entities.trainings.TrainingSession;

@Repository
public interface DeveloperTrainingSessionRepository extends AbstractRepository {

	@Query("select ts from TrainingSession ts")
	Collection<TrainingSession> findAllTrainingSessions();

	@Query("select ts from TrainingSession ts where ts.id = :id")
	TrainingSession findOneTrainingSessionById(int id);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :id")
	Collection<TrainingSession> findAllTrainingSessionsByTrainingModuleId(int id);

	@Query("select tm from TrainingModule tm")
	Collection<TrainingModule> findAllTrainingModules();

	@Query("select tm from TrainingModule tm where tm.id = :id")
	TrainingModule findOneTrainingModuleById(int id);

	@Query("select ts from TrainingSession ts where ts.code = :code")
	TrainingSession findOneTrainingSessionByCode(String code);

	@Query("select ts.trainingModule from TrainingSession ts where ts.id = :tsId")
	TrainingModule findOneTrainingModuleByTrainingSessionId(int tsId);

	@Query("select ts from TrainingSession ts where ts.trainingModule.id = :tmId")
	Collection<TrainingSession> findAllTrainingSessionsByMasterId(int tmId);
}
