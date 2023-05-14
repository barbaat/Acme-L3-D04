
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicums.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/praticumSession/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final int practicumSessionIndex, final String title, final String startPeriod, final String finishPeriod) {
		// HINT: this test authenticates as a company, then lists his or her practicums, 
		// HINT+ selects one of them, and check that it has the expected duties.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);
		super.checkInputBoxHasValue("code", practicumCode);
		super.clickOnButton("Practicum sessions");

		super.checkListingExists();
		super.checkColumnHasValue(practicumSessionIndex, 0, title);
		super.checkColumnHasValue(practicumSessionIndex, 1, startPeriod);
		super.checkColumnHasValue(practicumSessionIndex, 2, finishPeriod);
		super.clickOnListingRecord(practicumSessionIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.isDraftMode()) {
				param = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/list", param);
				super.checkPanicExists();
			}
	}

}
