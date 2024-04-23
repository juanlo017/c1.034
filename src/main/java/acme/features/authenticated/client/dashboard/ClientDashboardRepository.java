
package acme.features.authenticated.client.dashboard;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(p) from ProgressLog p where p.contract.client.id = :id")
	Integer numberOfProgressLogs(int id);

	@Query("select count(p) from ProgressLog p where p.contract.client.id = :id and p.completeness = 1")
	Integer numberOfCompletedProgressLogs(int id);

	@Query("select avg(c.budget) from Contract c where c.client.id = :id")
	Integer avgContractBudget(int id);

	@Query("select min(c.budget) from Contract c where c.client.id = :id")
	Integer minContractBudget(int id);

	@Query("select max(c.budget) from Contract c where c.client.id = :id")
	Integer maxContractBudget(int id);

	@Query("select stddev(c.budget) from Contract c where c.client.id = :id")
	Integer deviationContractBudget(int id);

}
