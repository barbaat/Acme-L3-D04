
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorials.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/show-positive1.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String abstractTutorial, final String goals, final String estimatedTotalTime, final String course) {
		// HINT: this test signs in as an assistant, lists the tutorials, click on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractTutorial", abstractTutorial);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/show-positive2.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test101Positive(final int recordIndex, final String code, final String title, final String abstractTutorial, final String goals, final String estimatedTotalTime, final String course) {
		// HINT: this test signs in as an assistant, lists the tutorials, click on  
		// HINT+ one of them, and checks that the form has the expected data.

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractTutorial", abstractTutorial);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// HINT+ that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a tutorial by a different user who is not the principal.

		Collection<Tutorial> tutorials;
		String param;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant2");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/show", param);
			super.checkPanicExists();
		}
	}

}
