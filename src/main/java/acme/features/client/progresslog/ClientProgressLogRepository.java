
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select pl from ProgressLog pl")
	Collection<ProgressLog> findAllProgressLogs();

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.client.id = :id")
	Collection<ProgressLog> findAllProgressLogsByClientId(int id);

	/*
	 * @Query("select p from ProgressLog where p.id = :id")
	 * ProgressLog findProgressLogByMasterId(int id);
	 * 
	 * @Query("select p from ProgressLog where p.contract.client.id = :id")
	 * Collection<ProgressLog> findAllProgressLogsByClientId(int id);
	 * 
	 * @Query("select p from ProgressLog where p.contract.id = :id")
	 * Collection<ProgressLog> findAllProgressLogsByContractId(int id);
	 */

}
