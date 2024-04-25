
package acme.features.developer.developerdashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DeveloperDashboardRepository extends AbstractRepository {

	@Query("select count(tm) from TrainingModule tm where tm.updateMoment is not null and tm.developer.id = :id and tm.draftMode = false")
	Integer totalNumberOfTrainingModulesWithUpdateMoment(int id);

	@Query("select count(ts) from TrainingSession ts where ts.link is not null and ts.trainingModule.developer.id = :id and ts.draftMode = false")
	Integer totalNumberOfTrainingSessionsWithLink(int id);

	@Query("select avg(tm.totalTime) from TrainingModule tm where tm.developer.id = :id and tm.draftMode = false")
	Double avgTimeOfTrainingModule(int id);

	@Query("select min(tm.totalTime) from TrainingModule tm where tm.developer.id = :id and tm.draftMode = false")
	Integer minTimeOfTrainingModule(int id);

	@Query("select max(tm.totalTime) from TrainingModule tm where tm.developer.id = :id and tm.draftMode = false")
	Integer maxTimeOfTrainingModule(int id);

	@Query("select stddev(tm.totalTime) from TrainingModule tm where tm.developer.id = :id and tm.draftMode = false")
	Double deviationTimeOfTrainingModule(int id);

}
