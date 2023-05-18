
package acme.testing.auditor.auditingRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditingRecords.AuditingRecord;
import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditingRecordUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		//Compruebo que puedo editar un auditingRecord correctamente

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkSubmitExists("Update");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Update");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(auditingRecordIndex, 0, subject);

		super.clickOnListingRecord(auditingRecordIndex);

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("startTime", startTime);
		super.checkInputBoxHasValue("finishTime", finishTime);
		super.checkInputBoxHasValue("mark", mark);
		super.checkInputBoxHasValue("moreInfo", moreInfo);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		//Compruebo que no puedo editar un correctionRecord

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkFormExists();

		final String auditingRecordIdString = super.getCurrentQuery();
		final int auditingRecordId = Integer.parseInt(auditingRecordIdString.substring(auditingRecordIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", auditingRecordId);

		super.checkNotSubmitExists("Update");

		super.request("/auditor/auditing-record/update", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/update-negative2.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test201Negative(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		//Compruebo que no puedo editar un auditingRecord con datos erróneos o vacíos

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnListingRecord(auditingRecordIndex);
		super.checkSubmitExists("Update");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits) {
			final Collection<AuditingRecord> auditingRecords = this.repository.findAuditingRecordsByAuditId(audit.getId());
			for (final AuditingRecord auditingRecord : auditingRecords)
				if (auditingRecord.getAudit().isDraftMode()) {
					final String param = String.format("id=%d", auditingRecord.getAudit().getId());

					super.checkLinkExists("Sign in");
					super.request("/auditor/auditing-record/update", param);
					super.checkPanicExists();

					//Si estoy autenticado como lecturer1
					super.signIn("lecturer1", "lecturer1");
					super.request("/auditor/auditing-record/update", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como auditor2
					super.signIn("auditor2", "auditor2");
					super.request("/auditor/auditing-record/update", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como student1
					super.signIn("student1", "student1");
					super.request("/auditor/auditingRecord/update", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}
}
