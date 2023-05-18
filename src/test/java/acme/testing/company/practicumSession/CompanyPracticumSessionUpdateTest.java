
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkSubmitExists("Update");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);

		super.clickOnSubmit("Update");

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
	@CsvFileSource(resources = "/company/practicumSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {
		//Compruebo que no puedo editar una sesión de prácticas excepcional

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkFormExists();

		final String practicumSessionIdString = super.getCurrentQuery();
		final int practicumSessionId = Integer.parseInt(practicumSessionIdString.substring(practicumSessionIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", practicumSessionId);

		super.checkNotSubmitExists("Update");

		super.request("/company/practicumSession/update", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-negative2.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test201Negative(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {
		//Compruebo que no puedo editar un practicumSession con datos erróneos o vacíos

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkSubmitExists("Update");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", abstract$);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("optionalLink", optionalLink);

		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Practicum> practicums = this.repository.findPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			final Collection<PracticumSession> practicumSessions = this.repository.findPracticumSessionsByPracticumId(practicum.getId());
			for (final PracticumSession practicumSession : practicumSessions)
				if (practicumSession.getPracticum().isDraftMode()) {
					final String param = String.format("id=%d", practicumSession.getPracticum().getId());

					super.checkLinkExists("Sign in");
					super.request("/company/practicumSession/update", param);
					super.checkPanicExists();

					//Si estoy autenticado como lecturer1
					super.signIn("lecturer1", "lecturer1");
					super.request("/company/practicumSession/update", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como company2
					super.signIn("company2", "company2");
					super.request("/company/practicumSession/update", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como auditor1
					super.signIn("auditor1", "auditor1");
					super.request("/company/practicumSession/update", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}

}
