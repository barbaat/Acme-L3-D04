
package acme.testing.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanyPracticumSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/company/practicumSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int auditIndex, final String code, final int auditingRecordIndex, final String subject, final String assessment, final String startTime, final String finishTime, final String mark, final String moreInfo) {
	//
	//		super.signIn("auditor1", "auditor1");
	//
	//		super.clickOnMenu("Auditor", "List of audits");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//
	//		super.checkColumnHasValue(auditIndex, 0, code);
	//		super.clickOnListingRecord(auditIndex);
	//		super.checkInputBoxHasValue("code", code);
	//
	//		super.clickOnButton("Auditing-Records");
	//		super.clickOnListingRecord(auditingRecordIndex);
	//		super.checkSubmitExists("Update");
	//
	//		super.fillInputBoxIn("subject", subject);
	//		super.fillInputBoxIn("assessment", assessment);
	//		super.fillInputBoxIn("startTime", startTime);
	//		super.fillInputBoxIn("finishTime", finishTime);
	//		super.fillInputBoxIn("mark", mark);
	//		super.fillInputBoxIn("moreInfo", moreInfo);
	//
	//		super.clickOnSubmit("Update");
	//
	//		super.clickOnMenu("Auditor", "List of audits");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//
	//		super.checkColumnHasValue(auditIndex, 0, code);
	//		super.clickOnListingRecord(auditIndex);
	//		super.checkInputBoxHasValue("code", code);
	//
	//		super.clickOnButton("Auditing-Records");
	//
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//		super.checkColumnHasValue(auditingRecordIndex, 0, subject);
	//
	//		super.clickOnListingRecord(auditingRecordIndex);
	//
	//		super.checkInputBoxHasValue("subject", subject);
	//		super.checkInputBoxHasValue("assessment", assessment);
	//		super.checkInputBoxHasValue("startTime", startTime);
	//		super.checkInputBoxHasValue("finishTime", finishTime);
	//		super.checkInputBoxHasValue("mark", mark);
	//		super.checkInputBoxHasValue("moreInfo", moreInfo);
	//
	//		super.signOut();
	//	}

}
