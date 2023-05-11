
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantSessionRepository extends AbstractRepository {

	@Query("select s from Session s where s.id = :id")
	Session findSessionById(@Param("id") int id);

	@Query("select t from Tutorial t where t.id = :tutorialId")
	Tutorial findTutorialById(@Param("tutorialId") int tutorialId);

	@Query("select s.tutorial from Session s where s.id = :id")
	Tutorial findTutorialBySessionId(@Param("id") int id);

	@Query("select a from Assistant a where a.id = :id")
	Assistant findAssistantById(@Param("id") int id);

	@Query("select s from Session s where s.tutorial.id = :tutorialId")
	Collection<Session> findManySessionsByTutorialId(@Param("tutorialId") int tutorialId);

	@Query("select t from Tutorial t")
	Collection<Tutorial> findAllTutorials();

}
