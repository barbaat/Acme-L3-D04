
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecord;
import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		// HINT: this test signs in as an auditor, lists his or her audits, selects
		// HINT+ one of them and checks that it's as expected.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.checkListingExists();
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("startTime", startTime);
		super.checkInputBoxHasValue("finishTime", finishTime);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

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
		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			final Collection<AuditingRecord> auditingRecords = this.repository.findAuditingRecordsByAuditId(audit.getId());
			for (final AuditingRecord auditingRecord : auditingRecords)
				if (auditingRecord.getAudit().isDraftMode()) {
					param = String.format("id=%d", auditingRecord.getAudit().getId());

					//Si no estoy autenticado
					super.checkLinkExists("Sign in");
					super.request("/auditor/auditing-record/show", param);
					super.checkPanicExists();

					//Si estoy autenticado como lecturer
					super.signIn("lecturer1", "lecturer1");
					super.request("/auditor/auditing-record/show", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como auditor2
					super.signIn("auditor2", "auditor2");
					super.request("/auditor/auditing-record/show", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("student1", "student1");
					super.request("/auditor/auditingRecord/show", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}
}
