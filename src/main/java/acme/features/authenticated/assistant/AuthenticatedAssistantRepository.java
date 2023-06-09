
package acme.features.authenticated.assistant;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AuthenticatedAssistantRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :userAccountId")
	UserAccount findOneUserAccountById(@Param("userAccountId") int userAccountId);

	@Query("select a from Assistant a where a.userAccount.id = :id")
	Assistant findOneAssistantByUserAccountId(@Param("id") int id);

}
