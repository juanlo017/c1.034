
package acme.features.client.progresslog;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

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
