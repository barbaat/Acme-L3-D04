
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordListTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String correctionRecord) {
		//Compruebo que puedo listar auditingRecords correctamente y que las correctionRecords aparecen destacadas en el listado

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Auditing-Records");

		super.checkListingExists();
		super.checkColumnHasValue(auditingRecordIndex, 0, subject);
		super.checkColumnHasValue(auditingRecordIndex, 1, assessment);
		super.checkColumnHasValue(auditingRecordIndex, 2, correctionRecord);
		super.clickOnListingRecord(auditingRecordIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		//Compruebo que solo auditor1 puede ver el listado de auditingRecords de sus audits no publicadas

		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				final String param = String.format("id=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();
				super.signIn("student1", "student1");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/auditor/auditing-record/list", param);
				super.checkPanicExists();
				super.signOut();

			}
	}
}
