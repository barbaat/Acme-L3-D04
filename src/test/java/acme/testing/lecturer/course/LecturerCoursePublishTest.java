
package acme.testing.lecturer.course;

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
}

//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to publish a lecture with a role other than "lecturer".
//
//		Collection<Lecture> lectures;
//		String params;
//
//		lectures = this.repository.findManyLecturesByLecturerUsername("user-account-lecturer2");
//		for (final Lecture leccion : lectures)
//			if (leccion.isDraftMode()) {
//				params = String.format("id=%d", leccion.getId());
//
//				super.checkLinkExists("Sign in");
//				super.request("/lecturer/lecture/publish", params);
//				super.checkPanicExists();
//
//				super.signIn("administrator", "administrator");
//				super.request("/lecturer/lecture/publish", params);
//				super.checkPanicExists();
//				super.signOut();
//			}
//	}
//
//	@Test
//	public void test301Hacking() {
//		// HINT: this test tries to publish a published lecture that was registered by the principal.
//
//		Collection<Lecture> lectures;
//		String params;
//
//		super.signIn("lecturer1", "lecturer1");
//		lectures = this.repository.findManyLecturesByLecturerUsername("user-account-lecturer1");
//		for (final Lecture leccion : lectures)
//			if (!leccion.isDraftMode()) {
//				params = String.format("id=%d", leccion.getId());
//				super.request("/lecturer/lecture/publish", params);
//			}
//		super.signOut();
//	}
//
//	@Test
//	public void test302Hacking() {
//
//		Collection<Lecture> lectures;
//		String params;
//
//		super.signIn("lecturer2", "lecturer2");
//		lectures = this.repository.findManyLecturesByLecturerUsername("user-account-lecturer1");
//		for (final Lecture lecture : lectures) {
//			params = String.format("id=%d", lecture.getId());
//			super.request("/lecturer/lecture/publish", params);
//		}
//		super.signOut();
//	}
