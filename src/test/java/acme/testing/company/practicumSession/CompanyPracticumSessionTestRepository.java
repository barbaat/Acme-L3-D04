
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumSessionTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = :username")
	Collection<Practicum> findPracticumsByCompanyUsername(String username);

	@Query("select ps from PracticumSession ps where ps.practicum.id=:id")
	Collection<PracticumSession> findPracticumSessionsByPracticumId(int id);

}
