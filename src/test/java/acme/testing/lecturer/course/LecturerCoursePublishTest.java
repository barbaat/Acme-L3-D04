
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.courses.Course;
import acme.testing.TestHarness;

public class LecturerCoursePublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int lectureIndex, final String code, final String title, final String abstractLecture, final String estimatedLearningTimeInHours) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkButtonExists("Lectures of course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureIndex, 0, title);
		super.checkColumnHasValue(lectureIndex, 1, abstractLecture);
		super.checkColumnHasValue(lectureIndex, 2, estimatedLearningTimeInHours);

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();

		super.clickOnSubmit("Publish a course");

		final Course curso = this.repository.findCourseByCode(code);
		final String params = String.format("id=%d", curso.getId());
		super.request("/lecturer/course/show", params);
		super.checkFormExists();
		super.checkNotButtonExists("Publish a course");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-negative-no-lectures.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200NegativeNoLectures(final int courseIndex, final String code) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkButtonExists("Lectures of course");
		super.clickOnButton("Lectures of course");

		super.checkListingEmpty();

		final Course curso = this.repository.findCourseByCode(code);
		final String params = String.format("id=%d", curso.getId());
		super.request("/lecturer/course/show", params);
		super.checkFormExists();
		super.clickOnSubmit("Publish a course");
		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-negative-theory-lectures.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test201NegativeTheoryLectures(final int courseIndex, final int lectureIndex, final String code, final String title, final String lectureType) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkButtonExists("Lectures of course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureIndex, 0, title);

		super.clickOnListingRecord(lectureIndex);
		super.checkInputBoxHasValue("lectureType", lectureType);

		final Course curso = this.repository.findCourseByCode(code);
		final String params = String.format("id=%d", curso.getId());
		super.request("/lecturer/course/show", params);
		super.checkFormExists();
		super.clickOnSubmit("Publish a course");
		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/publish-negative-published-lectures.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test202NegativePublishedLectures(final int courseIndex, final int lectureIndex, final String code, final String title) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkButtonExists("Lectures of course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);

		super.checkFormExists();
		super.checkNotButtonExists("Delete a lecture"); //Por lo que está publicada la lección

		final Course curso = this.repository.findCourseByCode(code);
		final String params = String.format("id=%d", curso.getId());
		super.request("/lecturer/course/show", params);
		super.checkFormExists();
		super.clickOnSubmit("Publish a course");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// Publicar curso con otro rol que no es Lecturer

		Collection<Course> cursos;
		String params;

		cursos = this.repository.findManyCoursesByLecturerUsername("lecturer2");
		for (final Course curso : cursos)
			if (curso.isDraftMode()) {
				params = String.format("id=%d", curso.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// Publicar un curso que ya está publicado

		Collection<Course> cursos;
		String params;

		super.signIn("lecturer2", "lecturer2");
		cursos = this.repository.findManyCoursesByLecturerUsername("lecturer2");
		for (final Course curso : cursos)
			if (!curso.isDraftMode()) {
				params = String.format("id=%d", curso.getId());
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		//Publicar un curso siendo Lecturer pero no el que lo creó
		Collection<Course> cursos;
		String params;

		super.signIn("lecturer1", "lecturer1");
		cursos = this.repository.findManyCoursesByLecturerUsername("lecturer2");
		for (final Course curso : cursos) {
			params = String.format("id=%d", curso.getId());
			super.request("/lecturer/course/publish", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}
