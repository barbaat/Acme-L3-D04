
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audits.Audit;
import acme.framework.repositories.AbstractRepository;

public interface AuditorAuditingRecordTestRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.userAccount.username = :username")
	Collection<Audit> findAuditsByAuditorUsername(String username);

}
