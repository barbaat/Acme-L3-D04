
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanyPracticumListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final String practicumCode, final String title, final String course) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List of practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumIndex, 0, practicumCode);
		super.clickOnListingRecord(practicumIndex);

		super.checkInputBoxHasValue("code", practicumCode);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("course", course);

		super.signOut();
	}

	//Duda: ¿Este test se hace?
	@Test
	public void test200Negative() {
		// No puede haber		
	}

	//Duda: ¿Este test se hace?
	@Test
	public void test300Hacking() {
		// Preguntar al profesor
	}

}
