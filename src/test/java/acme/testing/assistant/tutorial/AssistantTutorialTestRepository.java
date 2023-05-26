
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.framework.repositories.AbstractRepository;

public interface AssistantTutorialTestRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.userAccount.username = :username")
	Collection<Tutorial> findManyTutorialsByAssistantUsername(@Param("username") String username);

	@Query("select s from Session s where s.tutorial.assistant.userAccount.username = :username")
	Collection<Session> findManySessionsByAssistantUsername(@Param("username") String username);

	@Query("select t from Tutorial t where t.code = :code")
	Tutorial findTutorialByCode(@Param("code") String code);

	@Query("select t from Tutorial t")
	Collection<Tutorial> findAllTutorials();

}
