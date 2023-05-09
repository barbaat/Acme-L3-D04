
package acme.features.student.course;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentOnCourseRepository extends AbstractRepository {

	@Query("select o from Student o")
	List<Student> findAllStudent();

	@Query("select o from Course o where o.draftMode = false")
	List<Course> findAllPublishedCourses();

	@Query("select o from Course o where o.id = ?1")
	Course findCourseById(Integer id);

	@Query("select o from Student o where o.id = ?1")
	Student findStudentById(Integer id);
}
