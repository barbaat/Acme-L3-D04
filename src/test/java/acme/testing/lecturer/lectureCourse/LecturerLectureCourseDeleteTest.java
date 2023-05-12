
package acme.testing.lecturer.lectureCourse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureCourseDeleteTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link, final String draftMode) {

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Lecturer", "Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractLecture", abstractLecture);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create lecture");

		super.clickOnMenu("Lecturer", "List of lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);

		super.clickOnListingRecord(lectureIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractLecture", abstractLecture);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.checkInputBoxHasValue("lectureType", lectureType);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("draftMode", draftMode);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int lectureIndex, final String title, final String abstractLecture, final String body, final String estimatedLearningTimeInHours, final String lectureType, final String link) {

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Lecturer", "Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractLecture", abstractLecture);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("estimatedLearningTimeInHours", estimatedLearningTimeInHours);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create lecture");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

	}

}
