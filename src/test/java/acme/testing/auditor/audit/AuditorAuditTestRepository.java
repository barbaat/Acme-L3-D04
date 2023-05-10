
package acme.testing.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audits.Audit;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

public interface AuditorAuditTestRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.userAccount.id = :id")
	Collection<Audit> findAuditsByAuditorId(int id);

	@Query("select a from Auditor a where a.userAccount.username = :username")
	Auditor findAuditorByUsername(String username);

}
