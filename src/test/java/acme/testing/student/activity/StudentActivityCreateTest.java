
package acme.testing.student.activity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentActivityCreateTest extends TestHarness {

	// Test methods -----------------------------------------------------------

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
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
		super.clickOnButton("Create activity");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractt", abstractt);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("endPeriod", endPeriod);

		super.clickOnSubmit("Create activity");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("See workbook");
		super.clickOnButton("Activities");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(activityIndex, 0, title);

		super.clickOnListingRecord(activityIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractt", abstractt);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("endPeriod", endPeriod);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int enrolmentIndex, final String code, final int activityIndex, final String title, final String abstractt, final String activityType, final String link, final String startPeriod, final String endPeriod) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("See workbook");
		super.clickOnButton("Activities");

		super.clickOnButton("Create activity");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractt", abstractt);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("endPeriod", endPeriod);
		super.clickOnSubmit("Create activity");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/student/activity/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/activity/create");
		super.checkPanicExists();
		super.signOut();
	}
}
