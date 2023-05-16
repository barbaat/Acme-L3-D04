
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String moment, final String title, final String nick, final String message) {
		// HINT: this test lists the peeps,
		// HINT+ and then checks that the listing has the expected data.

		// Primero comprobamos que siendo anonymous podemos acceder

		super.checkLinkExists("Sign in");

		super.clickOnMenu("Any", "Peep list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);

		// Comprobamos que logueandonos en la aplicacion tambien nos lo muestra

		super.signIn("assistant1", "assistant1");
		super.request("/any/peep/list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();

		super.signIn("administrator", "administrator");
		super.request("/any/peep/list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/any/peep/list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/any/peep/list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/any/peep/list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();

		super.signIn("company1", "company1");
		super.clickOnMenu("Any", "Peep list");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: there aren't any hacking tests for this feature because it's a listing
		// HINT+ that doesn't involve being logged in with an specific role.
		// HINT+ Anyone can have access to it.
	}
}
