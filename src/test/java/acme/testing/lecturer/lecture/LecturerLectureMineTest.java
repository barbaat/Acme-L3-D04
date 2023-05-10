
//
//package acme.testing.lecturer.lecture;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//
//import acme.testing.TestHarness;
//
//public class LecturerLectureMineTest extends TestHarness {
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/employer/job/list-mine-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int recordIndex, final String reference, final String title, final String deadline) {
//		// HINT: this test authenticates as an employer, lists his or her jobs only,
//		// HINT+ and then checks that the listing has the expected data.
//
//		super.signIn("employer1", "employer1");
//
//		super.clickOnMenu("Employer", "List my jobs");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(recordIndex, 0, reference);
//		super.checkColumnHasValue(recordIndex, 1, deadline);
//		super.checkColumnHasValue(recordIndex, 2, title);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there aren't any negative tests for this feature since it's a listing that
//		// HINT+ doesn't involve entering any data into any forms.
//	}
//
//	@Test
//	public void test300Hacking() {
//		super.checkLinkExists("Sign in");
//		super.request("/employer/job/list-mine");
//		super.checkPanicExists();
//
//		super.signIn("administrator", "administrator");
//		super.request("/employer/job/list-mine");
//		super.checkPanicExists();
//		super.signOut();
//
//		super.signIn("worker1", "worker1");
//		super.request("/employer/job/list-mine");
//		super.checkPanicExists();
//		super.signOut();
//	}
//
//}
