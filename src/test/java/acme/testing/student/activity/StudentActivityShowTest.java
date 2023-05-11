
package acme.testing.student.activity;

//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.testing.TestHarness;
//
//public class StudentActivityShowTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected StudentActivityTestRepository repository;
//
//
//	// Test methods -----------------------------------------------------------
//	@ParameterizedTest
//	@CsvFileSource(resources = "/student/activity/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int enrolmentIndex, final String code, final int activityIndex, final String title, final String abstractt, final String activityType, final String moreInfo, final String startPeriod, final String endPeriod) {
//
//		super.signIn("employer1", "employer1");
//
//		super.clickOnMenu("Student", "List of enrolments");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//		super.clickOnListingRecord(enrolmentIndex);
//		super.clickOnButton("See workbook");
//		super.checkListingExists();
//		super.clickOnListingRecord(enrolmentIndex);
//		super.checkFormExists();
//
//		super.checkInputBoxHasValue("title", title);
//		super.checkInputBoxHasValue("description", description);
//		super.checkInputBoxHasValue("workLoad", workLoad);
//		super.checkInputBoxHasValue("moreInfo", moreInfo);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there's no negative test case for this listing, since it doesn't
//		// HINT+ involve filling in any forms.
//	}
//
//}
