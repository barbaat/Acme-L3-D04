
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.activities.Activity;
import acme.entities.enrolments.Enrolment;
import acme.testing.TestHarness;

public class StudentActivityShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final int activityIndex, final String title, final String abstractt, final String activityType, final String link, final String startPeriod, final String endPeriod) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("See workbook");
		super.clickOnButton("Activities");
		super.checkListingExists();
		super.clickOnListingRecord(activityIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractt", abstractt);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("endPeriod", endPeriod);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}
	@Test
	public void test300Hacking() {

		String param;
		final Collection<Enrolment> enrolments = this.repository.findManyEnrolmentsByStudentUsername("student1");

		for (final Enrolment enrolment : enrolments) {
			final Collection<Activity> activities = this.repository.findActivitiesByEnrolmentId(enrolment.getId());
			for (final Activity activity : activities)
				if (activity.getEnrolment().isDraftMode()) {
					param = String.format("id=%d", activity.getEnrolment().getId());

					//Si no estoy autenticado
					super.checkLinkExists("Sign in");
					super.request("/student/activity/show", param);
					super.checkPanicExists();

					super.signIn("administrator", "administrator");
					super.request("/student/enrolment/list");
					super.checkPanicExists();
					super.signOut();

					super.signIn("auditor1", "auditor1");
					super.request("/student/enrolment/list");
					super.checkPanicExists();
					super.signOut();

					super.signIn("lecturer1", "lecturer1");
					super.request("/student/enrolment/list");
					super.checkPanicExists();
					super.signOut();

					super.signIn("company1", "company1");
					super.request("/student/enrolment/list");
					super.checkPanicExists();
					super.signOut();

					super.signIn("assistant1", "assistant1");
					super.request("/student/enrolment/list");
					super.checkPanicExists();
					super.signOut();
				}
		}
	}
}
