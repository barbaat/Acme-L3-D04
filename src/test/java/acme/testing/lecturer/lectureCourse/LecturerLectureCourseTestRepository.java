
package acme.testing.lecturer.lectureCourse;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.courses.Course;
import acme.entities.lectures.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

public interface LecturerLectureCourseTestRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.userAccount.username = :username")
	Collection<Course> findManyCoursesByLecturerUsername(String username);

	@Query("select c from Lecture c where c.lecturer.userAccount.username = :username")
	Collection<Lecture> findManyLecturesByLecturerUsername(String username);

	@Query("select c from Course c where c.lecturer = :lecturer")
	Collection<Course> findManyCoursesByLecturer(Lecturer lecturer);

	@Query("select c from Course c where c.code = :code")
	Course findOneCourseByCode(String code);

	@Query("select l from Lecture l inner join LectureCourse lc on l = lc.lecture inner join Course c on lc.course = c where c.id = :masterId")
	Collection<Lecture> findManyLecturesByCourseId(int masterId);

}
