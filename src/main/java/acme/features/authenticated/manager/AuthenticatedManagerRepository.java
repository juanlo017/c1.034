
package acme.features.authenticated.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("")
	UserAccount findOneUserAccountById(int userAccountId);

}
