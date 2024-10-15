
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.contracts.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.id = :id")
	Collection<Contract> findContractsByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findContractById(int id);

	@Query("select c from Contract c where c.code LIKE :contractCode")
	Contract findContractByCode(String contractCode);

	@Query("select c from Client c where c.id = :id")
	Client findClientById(int id);

	@Query("select c from Contract c")
	Collection<Contract> findAllContracts();

	@Query("select c from Contract c where c.project.code LIKE :projectCode AND c.draftMode = false")
	Collection<Contract> findPublishedContractsByProjectCode(String projectCode);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findAllPublishedProjects();

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select c.project from Contract c where c.project.id = :id")
	Project findProjectByContractId(int id);

	@Query("select p from ProgressLog p where p.contract.id = :id")
	Collection<ProgressLog> findProgressLogsByContractId(int id);

	@Query("select c.acceptedCurrencies from Configuration c")
	String findAcceptedCurrencies();

}
