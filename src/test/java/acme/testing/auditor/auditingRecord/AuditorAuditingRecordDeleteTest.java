
package acme.testing.auditor.auditingRecord;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditorAuditingRecordDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final int auditingRecordId, final String subject, final String assessment, final String startTime, final String finishTime, final String mark,
		final String moreInfo) {

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

		final String param = String.format("id=%d", auditingRecordId);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/auditor/auditingRecord/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String code, final int auditingRecordIndex, final int auditingRecordId, final String subject, final String assessment, final String startTime, final String finishTime, final String mark,
		final String moreInfo) {

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

		final String param = String.format("id=%d", auditingRecordId);

		super.checkNotButtonExists("Delete");

		super.request("/auditor/auditingRecord/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	//	@Test
	//	public void test300Hacking() {
	//
	//		Collection<AuditingRecord> auditingRecords;
	//		String param;
	//
	//		auditingRecords = this.repository.findAuditingRecordsByAuditorUsername("auditor1");
	//		for (final AuditingRecord leccion : auditingRecords) {
	//			param = String.format("id=%d", leccion.getId());
	//
	//			super.checkLinkExists("Sign in");
	//			super.request("/auditor/auditingRecord/delete", param);
	//			super.checkPanicExists();
	//
	//			super.signIn("administrator", "administrator");
	//			super.request("/auditor/auditingRecord/delete", param);
	//			super.checkPanicExists();
	//			super.signOut();
	//
	//			super.signIn("auditor2", "auditor2");
	//			super.request("/auditor/auditingRecord/delete", param);
	//			super.checkPanicExists();
	//			super.signOut();
	//		}
	//	}
}
