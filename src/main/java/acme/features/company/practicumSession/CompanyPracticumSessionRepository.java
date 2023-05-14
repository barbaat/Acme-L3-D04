
package acme.features.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface CompanyPracticumSessionRepository extends AbstractRepository {

	//Si
	@Query("select p from Practicum p where p.id = :id")
	Practicum findPracticumById(int id);

	@Query("select p from Practicum p where p.company.id = :id And p.draftMode = false")
	Collection<Practicum> findManyPublishedPracticaByCompanyId(int id);

	@Query("select ps from PracticumSession ps where ps.practicum.id = :id And ps.exceptional = true")
	Collection<PracticumSession> findExceptionalSessionsByPracticumId(int id);

	@Query("select p from Practicum p where p.company.id = :id And p.draftMode = true")
	Collection<Practicum> findManyPrivatePracticaByCompanyId(int id);

	//Si
	@Query("select ps from PracticumSession ps where ps.practicum.id = :id")
	Collection<PracticumSession> findPracticumSessionsByPracticumId(int id);

	@Query("select ps.practicum from PracticumSession ps where ps.id = :id")
	Practicum findPracticumByPracticumSessionId(int id);

	//Si
	@Query("select ps from PracticumSession ps where ps.id = :id")
	PracticumSession findPracticumSessionById(int id);

	//	@Query("select ps from PracticumSession ps where ps.practicum.id = :id")
	//	Collection<PracticumSession> findManyPracticumSessionsByPracticumId(int id);

	@Query("select p from Practicum p where p.company.id = :id")
	Collection<Practicum> findManyPracticaByCompanyId(int id);

}
