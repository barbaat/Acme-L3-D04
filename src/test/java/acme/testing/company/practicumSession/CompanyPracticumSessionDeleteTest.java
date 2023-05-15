
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.checkListingExists();
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("optionalLink", optionalLink);

		final String practicumSessionIdString = super.getCurrentQuery();
		final int practicumSessionId = Integer.parseInt(practicumSessionIdString.substring(practicumSessionIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", practicumSessionId);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/company/practicumSession/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.checkListingExists();
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("optionalLink", optionalLink);

		final String practicumSessionIdString = super.getCurrentQuery();
		final int practicumSessionId = Integer.parseInt(practicumSessionIdString.substring(practicumSessionIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", practicumSessionId);

		super.checkNotSubmitExists("Delete");

		super.request("/company/practicumSession/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Practicum> practicums = this.repository.findPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			final Collection<PracticumSession> practicumSessions = this.repository.findPracticumSessionsByPracticumId(practicum.getId());
			for (final PracticumSession practicumSession : practicumSessions) {
				final String param = String.format("id=%d", practicumSession.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicumSession/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicumSession/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicumSession/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicumSession/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
		}
	}

}
