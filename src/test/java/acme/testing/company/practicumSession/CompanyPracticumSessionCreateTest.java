
package acme.testing.company.practicumSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanyPracticumSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnButton("Create practicum session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);

		super.clickOnSubmit("Create practicum session");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(practicumSessionIndex, 0, title);

		super.clickOnListingRecord(practicumSessionIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("optionalLink", optionalLink);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-positive-exceptional.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100PositiveExceptional(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink,
		final String confirmation) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnButton("Create exceptional session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);
		super.fillInputBoxIn("confirmation", confirmation);

		super.clickOnSubmit("Create exceptional session");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumSessionIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("optionalLink", optionalLink);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnButton("Create practicum session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);

		super.clickOnSubmit("Create practicum session");

		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/create-negative-exceptional.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200NegativeExceptional(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnButton("Create exceptional session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);

		super.clickOnSubmit("Create exceptional session");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/company/practicumSession/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/company/practicumSession/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicumSession/create");
		super.checkPanicExists();
		super.signOut();
	}

}
