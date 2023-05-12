
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lectures.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractLecture", abstractLecture);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update a lecture");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureIndex, 0, title);
		super.checkColumnHasValue(lectureIndex, 1, abstractLecture);
		super.checkColumnHasValue(lectureIndex, 2, estimatedLearningTimeInHours);

		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractLecture", abstractLecture);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.checkInputBoxHasValue("lectureType", lectureType);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link) {
		// HINT: this test attempts to update a lecture with wrong data.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractLecture", abstractLecture);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update a lecture");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer2");
		for (final Lecture leccion : lectures) {
			param = String.format("id=%d", leccion.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
