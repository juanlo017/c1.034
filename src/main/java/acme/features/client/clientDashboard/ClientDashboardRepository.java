
package acme.features.client.clientDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(p) from ProgressLog p where p.contract.client.id = :id")
	Integer numberOfProgressLogs(int id);

	@Query("select count(p) from ProgressLog p where p.contract.client.id = :id and p.completeness = 1")
	Integer numberOfCompletedProgressLogs(int id);

	@Query("select avg(c.budget.amount) from Contract c where c.client.id = :id")
	Double avgContractBudget(int id);

	@Query("select min(c.budget.amount) from Contract c where c.client.id = :id")
	Double minContractBudget(int id);

	@Query("select max(c.budget.amount) from Contract c where c.client.id = :id")
	Double maxContractBudget(int id);

	@Query("select stddev(c.budget.amount) from Contract c where c.client.id = :id")
	Double deviationContractBudget(int id);

	@Query("select c.acceptedCurrencies from Configuration c")
	String findAcceptedCurrencies();

	@Query("select avg(c.budget.amount) from Contract c where c.client.id = :id and c.budget.currency like :currency")
	Double avgContractBudgetForCurrency(int id, String currency);

	@Query("select min(c.budget.amount) from Contract c where c.client.id = :id and c.budget.currency like :currency")
	Double minContractBudgetForCurrency(int id, String currency);

	@Query("select max(c.budget.amount) from Contract c where c.client.id = :id and c.budget.currency like :currency")
	Double maxContractBudgetForCurrency(int id, String currency);

	@Query("select stddev(c.budget.amount) from Contract c where c.client.id = :id and c.budget.currency like :currency")
	Double deviationContractBudgetForCurrency(int id, String currency);

}
