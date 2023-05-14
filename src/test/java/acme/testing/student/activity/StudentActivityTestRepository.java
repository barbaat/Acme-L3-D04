
package acme.testing.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.activities.Activity;
import acme.entities.enrolments.Enrolment;
import acme.framework.repositories.AbstractRepository;

public interface StudentActivityTestRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.student.userAccount.username = :username")
	Collection<Enrolment> findManyEnrolmentsByStudentUsername(String username);

	@Query("select a from Activity a where a.enrolment.id=:id")
	Collection<Activity> findActivitiesByEnrolmentId(int id);

	@Query("SELECT a FROM Activity a WHERE a.id = :id")
	Activity findActivityById(String id);
}
