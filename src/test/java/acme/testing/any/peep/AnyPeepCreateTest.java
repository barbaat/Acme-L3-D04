
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String moment, final String title, final String nick, final String message, final String email, final String link) {
		// HINT: this test lists peeps, creates a new one, and check that it's been created properly.

		// Primero probamos con anonymous

		super.checkLinkExists("Sign in");

		super.clickOnMenu("Any", "Create peep");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.clickOnMenu("Any", "Peep list");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		// Comprobando con Create peep

		super.checkLinkExists("Sign in");

		super.clickOnMenu("Any", "Create peep");

		super.checkInputBoxHasValue("nick", null);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.clickOnMenu("Any", "Peep list");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		// Comprobamos logueandonos

		super.signIn("assistant1", "assistant1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.checkInputBoxHasValue("nick", "assistant1");
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.clickOnMenu("Any", "Peep list");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, moment);
		super.checkColumnHasValue(recordIndex, 1, title);
		super.checkColumnHasValue(recordIndex, 2, nick);
		super.checkColumnHasValue(recordIndex, 3, message);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("moment", moment);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String moment, final String nick, final String title, final String message, final String email, final String link) {
		// HINT: this test attempts to create peeps with incorrect data.

		super.checkLinkExists("Sign in");

		super.clickOnMenu("Any", "Peep list");
		super.clickOnButton("Publish");
		super.checkFormExists();

		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		// Comprobamos con los otros roles

		super.signIn("assistant1", "assistant1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		super.signIn("administrator", "administrator");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		super.signIn("employer1", "employer1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();

		super.signIn("company1", "company1");
		super.request("/any/peep/create");

		super.checkFormExists();
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: there aren't any hacking tests for this feature because it's a listing
		// HINT+ that doesn't involve being logged in with an specific role.
		// HINT+ Anyone can have access to it.
	}

}
