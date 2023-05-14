
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/praticumSession/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String abstract$, final String startPeriod, final String finishPeriod, final String optionalLink) {
		// HINT: this test signs in as a company, lists his or her practicums, selects
		// HINT+ one of them and checks that it's as expected.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);

		super.clickOnButton("Practicum sessions");
		super.checkListingExists();
		super.clickOnListingRecord(practicumIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", abstract$);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("optionalLink", optionalLink);

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
		final Collection<Practicum> practicums = this.repository.findPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			final Collection<PracticumSession> practicumSessions = this.repository.findPracticumSessionsByPracticumId(practicum.getId());
			for (final PracticumSession practicumSession : practicumSessions)
				if (practicumSession.getPracticum().isDraftMode()) {
					param = String.format("id=%d", practicumSession.getPracticum().getId());

					//Si no estoy autenticado
					super.checkLinkExists("Sign in");
					super.request("/company/practicum-session/show", param);
					super.checkPanicExists();

					//Si estoy autenticado como lecturer
					super.signIn("lecturer1", "lecturer1");
					super.request("/company/practicum-session/show", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como company2
					super.signIn("company2", "company2");
					super.request("/company/practicum-session/show", param);
					super.checkPanicExists();
					super.signOut();

					//Si estoy autenticado como auditor
					super.signIn("auditor1", "auditor1");
					super.request("/company/practicum-session/show", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}

}
