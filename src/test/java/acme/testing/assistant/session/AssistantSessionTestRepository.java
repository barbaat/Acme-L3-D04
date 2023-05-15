
package acme.testing.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AssistantSessionTestRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :username")
	Collection<Tutorial> findManyTutorialsByAssistantUsername(@Param("username") String username);

	@Query("select s from Session s where s.tutorial.assistant.userAccount.username = :username")
	Collection<Session> findManySessionsByAssistantUsername(@Param("username") String username);

	@Query("select s from Session s where s.tutorial.id = :id")
	Collection<Session> findManySessionsByTutorialId(@Param("id") int id);

	@Query("select s from Session s where s.title = :title AND s.abstractSession = :abstractSession")
	Session findSessionByTitleAndAbstract(@Param("title") String title, @Param("abstractSession") String abstractSession);

}
