
package acme.testing.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuditorAuditListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final String conclusion) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.checkColumnHasValue(auditIndex, 1, conclusion);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the audits of an auditor as a
		// HINT+ principal with the wrong role.

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/list");
		super.checkPanicExists();

		super.checkLinkExists("Sign in");
		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();

		super.checkLinkExists("Sign in");
		super.signIn("student1", "student1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();
	}

	//
	//	@Autowired
	//	protected StudentEnrolmentTestRepository repository;
	//
	//	// Test methods -----------------------------------------------------------
	//
	//
	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/student/enrolment/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int enrolmentIndex, final String code, final String course) {
	//
	//		super.signIn("student1", "student1");
	//
	//		super.clickOnMenu("Student", "List of enrolments");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//
	//		super.checkColumnHasValue(enrolmentIndex, 0, code);
	//		super.clickOnListingRecord(enrolmentIndex);
	//
	//		super.checkInputBoxHasValue("code", code);
	//		super.checkInputBoxHasValue("course", course);
	//
	//		super.signOut();
	//	}
	//
	//	@Test
	//	public void test200Negative() {
	//		// No puede haber		
	//	}
	//
	//	@Test
	//	public void test300Hacking() {
	//		// No puede haber
	//	}
}
