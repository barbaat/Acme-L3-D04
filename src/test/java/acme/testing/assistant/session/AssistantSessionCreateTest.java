
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorials.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String abstractSession, final String isTheorySession, final String initTimePeriod, final String finishTimePeriod, final String link) {
		// HINT: this test authenticates as an assistant, list his or her tutorials, navigates
		// HINT+ to their sessions, and checks that they have the expected data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("isTheorySession", isTheorySession);
		super.fillInputBoxIn("initTimePeriod", initTimePeriod);
		super.fillInputBoxIn("finishTimePeriod", finishTimePeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, abstractSession);
		super.checkColumnHasValue(sessionRecordIndex, 2, isTheorySession);
		super.checkColumnHasValue(sessionRecordIndex, 3, initTimePeriod);
		super.checkColumnHasValue(sessionRecordIndex, 4, finishTimePeriod);
		super.checkColumnHasValue(sessionRecordIndex, 5, link);

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("isTheorySession", isTheorySession);
		super.checkInputBoxHasValue("initTimePeriod", initTimePeriod);
		super.checkInputBoxHasValue("finishTimePeriod", finishTimePeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String abstractSession, final String isTheorySession, final String initTimePeriod, final String finishTimePeriod, final String link) {
		// HINT: this test attempts to create sessions using wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("isTheorySession", isTheorySession);
		super.fillInputBoxIn("initTimePeriod", initTimePeriod);
		super.fillInputBoxIn("finishTimePeriod", finishTimePeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a session for a tutorial as a principal without 
		// HINT: the "Assistant" role.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("tutorialId=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("employer1", "employer1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to create a session for an unpublished tutorial created by 
		// HINT+ the principal.

		Collection<Tutorial> tutorials;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("tutorialId=%d", tutorial.getId());
				super.request("/assistant/session/create", param);
				super.checkPanicExists();
			}
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to create sessions for tutorials that weren't created 
		// HINT+ by the principal.

		final Collection<Tutorial> tutorials;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant2");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("tutorialId=%d", tutorial.getId());
			super.request("/assistant/session/create", param);
			super.checkPanicExists();
		}
	}

}
