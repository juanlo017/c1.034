
package acme.features.authenticated.client.contracts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Repository
public interface ClientContractsRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.id = :id")
	List<Contract> findContractsByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select c from Client c where c.id = :id")
	Client findClientById(int id);

}
