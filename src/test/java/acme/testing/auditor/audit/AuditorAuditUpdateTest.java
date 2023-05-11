
package acme.testing.auditor.audit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditorAuditUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(auditIndex, 0, code);
		super.checkColumnHasValue(auditIndex, 1, conclusion);

		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	//	@Test
	//	public void test300Hacking() {
	//
	//		Collection<Course> cursos;
	//		String param;
	//
	//		cursos = this.repository.findManyCoursesByLecturerUsername("user-account-auditor1");
	//		for (final Course curso : cursos) {
	//			param = String.format("id=%d", curso.getId());
	//
	//			super.checkLinkExists("Sign in");
	//			super.request("/auditor/course/update", param);
	//			super.checkPanicExists();
	//
	//			super.signIn("administrator", "administrator");
	//			super.request("/auditor/course/update", param);
	//			super.checkPanicExists();
	//			super.signOut();
	//
	//		}
	//	}
}
