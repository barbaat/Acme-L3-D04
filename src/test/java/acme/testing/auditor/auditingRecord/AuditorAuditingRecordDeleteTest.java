
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecord;
import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		//Compruebo que puedo eliminar un auditingRecord correctamente

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

		final String auditingRecordIdString = super.getCurrentQuery();
		final int auditingRecordId = Integer.parseInt(auditingRecordIdString.substring(auditingRecordIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", auditingRecordId);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/auditor/auditingRecord/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		//Compruebo que no puedo eliminar un correction record

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

		final String auditingRecordIdString = super.getCurrentQuery();
		final int auditingRecordId = Integer.parseInt(auditingRecordIdString.substring(auditingRecordIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", auditingRecordId);

		super.checkNotSubmitExists("Delete");

		super.request("/auditor/auditingRecord/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		//Compruebo que solo auditor1 puede eliminar sus auditingRecords

		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			final Collection<AuditingRecord> auditingRecords = this.repository.findAuditingRecordsByAuditId(audit.getId());
			for (final AuditingRecord auditingRecord : auditingRecords) {
				final String param = String.format("id=%d", auditingRecord.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/auditingRecord/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}
	}
}
