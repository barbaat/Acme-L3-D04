
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lectures.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractLecture", abstractLecture);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.checkInputBoxHasValue("lectureType", lectureType);
		super.checkInputBoxHasValue("link", link);
		final Lecture lecture = this.repository.findLectureByTitle(title);
		final String param = String.format("id=%d", lecture.getId());
		super.clickOnSubmit("Delete a lecture");
		super.checkNotErrorsExist();

		super.request("/lecturer/lecture/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractLecture", abstractLecture);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.checkInputBoxHasValue("lectureType", lectureType);
		super.checkInputBoxHasValue("link", link);

		super.checkNotButtonExists("Delete a lecture");

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture leccion : lectures) {
			param = String.format("id=%d", leccion.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer2", "lecturer2");
			super.request("/lecturer/lecture/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
