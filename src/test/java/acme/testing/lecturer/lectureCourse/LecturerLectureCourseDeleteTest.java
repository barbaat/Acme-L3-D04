
package acme.testing.lecturer.lectureCourse;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.courses.Course;
import acme.entities.lectures.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureCourseDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureCourseTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectureCourse/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int lectureCourseIndex, final String code, final String title, final String titleLectureNuevoRecord) {

		// Eliminar una lecture de un curso que no está publicado

		final Course curso = this.repository.findOneCourseByCode(code);
		final Collection<Lecture> lectures = this.repository.findManyLecturesByCourseId(curso.getId());

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Lecturer", "List of courses");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkSubmitExists("Publish a course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureCourseIndex, 0, title);
		super.clickOnListingRecord(lectureCourseIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.clickOnButton("Delete from course");

		super.checkFormExists();
		super.fillInputBoxIn("course", code);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.clickOnMenu("Lecturer", "List of courses");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkSubmitExists("Publish a course");
		super.clickOnButton("Lectures of course");

		//Hay que controlar cuando hay una lista de un solo elemento o de varios
		if (lectures.size() > 1) {
			super.checkListingExists();
			super.sortListing(0, "asc");
			super.checkColumnHasValue(lectureCourseIndex, 0, titleLectureNuevoRecord);
		} else if (lectures.size() == 1)
			super.checkListingEmpty();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectureCourse/delete-negative-published.csv", encoding = "utf-8", numLinesToSkip = 1)
	//Dejo "add-negative-published.csv" porque me sirve ese csv
	public void test200NegativePublishedCourse(final int courseIndex, final int lectureIndex, final int lectureCourseIndex, final String code, final String titleLectureCourse, final String titleLecture) {

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Lecturer", "List of courses");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkNotSubmitExists("Publish a course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureCourseIndex, 0, titleLectureCourse);

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, titleLecture);
		super.clickOnListingRecord(lectureIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", titleLecture);
		super.clickOnButton("Delete from course");

		super.checkFormExists();
		super.fillInputBoxIn("course", code);
		super.clickOnSubmit("Delete");
		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lectureCourse/delete-negative-not-course.csv", encoding = "utf-8", numLinesToSkip = 1)
	// Lo mismo que arriba
	public void test200NegativeLectureNotInCourse(final int courseIndex, final int lectureIndex, final String code, final String title) {

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Lecturer", "List of courses");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkSubmitExists("Publish a course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.clickOnButton("Delete from course");

		super.checkFormExists();
		super.fillInputBoxIn("course", code);
		super.clickOnSubmit("Delete");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		// No pueda añadir una lecture a un curso alguien que no tenga el rol de lecturer

		Collection<Lecture> lectures;
		String param;
		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer2");
		for (final Lecture lecture : lectures) {

			param = String.format("lectureId=%d", lecture.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture-course/create" + param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture-course/create" + param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {

		// No pueda añadir una lecture a un curso alguien que tenga el rol de Lecturer que creó la lecture

		Collection<Lecture> lectures;
		String param;

		super.signIn("lecturer1", "lecturer1");
		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer2");

		for (final Lecture lecture : lectures) {
			param = String.format("lectureId=%d", lecture.getId());
			super.request("/lecturer/lecture-course/create" + param);
			super.checkPanicExists();
		}
	}
}
