
package acme.testing.auditor.auditingRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditorAuditingRecordCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditingRecordTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnButton("Create");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);

		super.clickOnSubmit("Create");

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
	@CsvFileSource(resources = "/auditor/auditingRecord/create-positive-exceptional.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100PositiveExceptional(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo,
		final String confirmation) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnButton("Create correction record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.fillInputBoxIn("confirmation", confirmation);

		super.clickOnSubmit("Create correction record");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");

		super.checkListingExists();
		super.sortListing(0, "asc");

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
	@CsvFileSource(resources = "/auditor/auditingRecord/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnButton("Create");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditingRecord/create-negative-exceptional.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200NegativeExceptional(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.clickOnListingRecord(auditIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Auditing-Records");
		super.clickOnButton("Create correction record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("startTime", startTime);
		super.fillInputBoxIn("finishTime", finishTime);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("moreInfo", moreInfo);
		super.clickOnSubmit("Create correction record");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/auditor/auditingRecord/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/auditingRecord/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/auditingRecord/create");
		super.checkPanicExists();
		super.signOut();
	}
}
