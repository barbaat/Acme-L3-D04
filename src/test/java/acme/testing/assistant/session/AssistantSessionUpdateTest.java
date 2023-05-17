
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String code, final int sessionRecordIndex, final String title, final String abstractSession, final String isTheorySession, final String initTimePeriod, final String finishTimePeriod,
		final String link) {
		// HINT: this test logs in as an assistant, lists his or her sessions, 
		// HINT+ selects one of them, lists the sessions, selects one, updates it, and then checks that 
		// HINT+ the update has actually been performed.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.checkNotSubmitExists("Publish");

		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkSubmitExists("Update");

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("isTheorySession", isTheorySession);
		super.fillInputBoxIn("initTimePeriod", initTimePeriod);
		super.fillInputBoxIn("finishTimePeriod", finishTimePeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, abstractSession);

		if (isTheorySession.equals("true"))
			super.checkColumnHasValue(sessionRecordIndex, 2, "Yes");
		else
			super.checkColumnHasValue(sessionRecordIndex, 2, "No");

		super.checkColumnHasValue(sessionRecordIndex, 3, initTimePeriod);

		super.clickOnListingRecord(sessionRecordIndex);

		super.checkFormExists();
		super.checkSubmitExists("Publish");
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("isTheorySession", isTheorySession);
		super.checkInputBoxHasValue("initTimePeriod", initTimePeriod);
		super.checkInputBoxHasValue("finishTimePeriod", finishTimePeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final String code, final int sessionRecordIndex, final String title, final String abstractSession, final String isTheorySession, final String initTimePeriod, final String finishTimePeriod,
		final String link) {
		// HINT: this test attempts to update a session with wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("isTheorySession", isTheorySession);
		super.fillInputBoxIn("initTimePeriod", initTimePeriod);
		super.fillInputBoxIn("finishTimePeriod", finishTimePeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a session for a session as a principal without 
		// HINT: the "Assistant" role.

		String param;
		final Collection<Tutorial> tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			final Collection<Session> sessions = this.repository.findManySessionsByTutorialId(tutorial.getId());
			for (final Session session : sessions)
				if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {
					param = String.format("id=%d", session.getId());

					super.checkLinkExists("Sign in");
					super.request("/assistant/session/update", param);
					super.checkPanicExists();

					super.signIn("administrator", "administrator");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("assistant2", "assistant2");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("student1", "student1");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("lecturer1", "lecturer1");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("employer1", "employer1");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("auditor1", "auditor1");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("company1", "company1");
					super.request("/assistant/assistant/update", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update a published session created by 
		// HINT+ the principal.

		final Collection<Session> sessions;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final Session session : sessions)
			if (!session.getTutorial().isDraftMode() && !session.isDraftMode()) {
				param = String.format("id=%d", session.getId());
				super.request("/assistant/session/update", param);
				super.checkPanicExists();
			}
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to update sessions for tutorials that weren't created 
		// HINT+ by the principal.

		final Collection<Session> sessions;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findManySessionsByAssistantUsername("assistant2");
		for (final Session session : sessions) {
			param = String.format("id=%d", session.getId());
			super.request("/assistant/session/update", param);
			super.checkPanicExists();
		}
	}

}
