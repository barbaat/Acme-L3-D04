
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lectures.Lecture;
import acme.testing.TestHarness;

public class LecturerLectureShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link, final String draftMode) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractLecture", abstractLecture);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.checkInputBoxHasValue("lectureType", lectureType);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("draftMode", draftMode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// No puede tener
	}

	@Test
	public void test300Hacking() {
		/*
		 * Solo el lecturer que ha creado esa lecture puede verlo
		 */

		Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer2");
		for (final Lecture leccion : lectures)
			if (leccion.isDraftMode()) {
				param = String.format("id=%d", leccion.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/lecturer/lecture/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
