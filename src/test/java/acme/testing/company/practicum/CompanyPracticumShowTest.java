
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanyPracticumShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String course, final String practicumCode, final String title, final String abstract$, final String goals, final String estimatedTotalTime) {
		// HINT: this test signs in as a company, lists his or her practicums, selects one of them and checks that it's as expected.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", practicumCode);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// No puede tener
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	//	@Test
	//	public void test300Hacking() {

	// HINT: this test tries to show a duty of a job that is in draft mode or
	// HINT+ not available, but wasn't published by the principal;

	//		// Un profesor solo puede ver sus cursos
	//		Collection<Course> courses;
	//		String param;
	//
	//		super.signIn("lecturer1", "lecturer1");
	//		courses = this.repository.findManyCoursesByLecturerUsername("user-account-lecturer2");
	//		for (final Course course : courses)
	//			if (course.isDraftMode()) {
	//				param = String.format("id=%d", course.getId());
	//
	//				super.checkLinkExists("Sign in");
	//				super.request("/lecturer/course/show", param);
	//				super.checkPanicExists();
	//			}
	//	}
}
