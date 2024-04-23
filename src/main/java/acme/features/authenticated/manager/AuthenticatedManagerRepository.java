
package acme.features.authenticated.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.roles.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("SELECT m FROM Manager m WHERE m.userAccount.id = :userAccountId")
	Manager findManagerByUserAccountId(int userAccountId);

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id= :userAccountId")
	UserAccount findOneUserAccountById(int userAccountId);

}
