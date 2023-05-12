
package acme.testing.lecturer.lectureCourse;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.courses.Course;
import acme.entities.lectureCourses.LectureCourse;
import acme.entities.lectures.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

public interface LecturerLectureCourseTestRepository extends AbstractRepository {

	@Query("select c from Course c where c.lecturer.userAccount.username = :username")
	Collection<Course> findManyCoursesByLecturerUsername(String username);

	@Query("select c from Lecture c where c.lecturer.userAccount.username = :username")
	Collection<Lecture> findManyLecturesByLecturerUsername(String username);

	@Query("SELECT i FROM Lecture i WHERE i.title = :title")
	Lecture findLectureByTitle(String title);

	@Query("select lc from LectureCourse lc where lc.id = :id")
	LectureCourse findOneLectureCourseById(int id);

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select c from Course c where c.lecturer = :lecturer")
	Collection<Course> findManyCoursesByLecturer(Lecturer lecturer);

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findOneLectureById(int id);

	@Query("select l from Lecture l inner join LectureCourse lc on l = lc.lecture inner join Course c on lc.course = c where c.id = :masterId")
	Collection<Lecture> findManyLecturesByCourseId(int masterId);

	@Query("select lc from LectureCourse lc where lc.lecture = :lecture and lc.course = :course")
	LectureCourse findOneLectureCourseByLectureAndCourse(Lecture lecture, Course course);
}
