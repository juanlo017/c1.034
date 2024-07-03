
package acme.features.client.progresslog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Repository
public interface ClientProgressLogRepository extends AbstractRepository {

	@Query("select pl from ProgressLog pl")
	Collection<ProgressLog> findAllProgressLogs();

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select pl.contract from ProgressLog pl where pl.id = :id")
	Contract findContractByProgressLogId(int id);

	@Query("select pl from ProgressLog pl where pl.id = :id")
	ProgressLog findProgressLogById(int id);

	@Query("select pl from ProgressLog pl where pl.contract.client.id = :id")
	Collection<ProgressLog> findAllProgressLogsByClientId(int id);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findAllProgressLogsByContractId(int id);

	@Query("select c from Client c where c.id = :id")
	Client findClientById(int id);

	@Query("select pl from ProgressLog pl where pl.recordId LIKE :recordId")
	ProgressLog findProgressLogByRecordId(String recordId);

}
