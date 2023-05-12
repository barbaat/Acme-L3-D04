
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String mark) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);
		super.checkInputBoxHasValue("mark", mark);

		final Audit audit = this.repository.findAuditByCode(code);
		final String param = String.format("id=%d", audit.getId());
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/auditor/audit/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String mark) {
		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);
		super.checkInputBoxHasValue("mark", mark);

		super.checkNotSubmitExists("Delete");

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			final String param = String.format("id=%d", audit.getId());

			super.checkLinkExists("Sign in");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor2", "auditor2");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/auditor/audit/delete", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
