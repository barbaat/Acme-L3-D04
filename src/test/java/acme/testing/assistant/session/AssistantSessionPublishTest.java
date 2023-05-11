
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.Session;
import acme.testing.TestHarness;

public class AssistantSessionPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String code, final int sessionRecordIndex, final String title) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials,
		// HINT: then selects one of them, list the sessions, select one of them
		// HINT:, and publishes it.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(tutorialRecordIndex, 0, code);

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkFormExists();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final String code, final int sessionRecordIndex, final String title) {
		// HINT: this test attempts to publish a session that cannot be published, yet.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(tutorialRecordIndex, 0, code);

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkFormExists();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkAlertExists(false);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a session with a role other than "Assistant".

		Collection<Session> sessions;
		String param;

		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final Session session : sessions)
			if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {
				param = String.format("id=%d", session.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/session/publish", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/session/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("employer1", "employer1");
				super.request("/assistant/session/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/session/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/session/publish", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published session that was registered by the principal.

		Collection<Session> sessions;
		String param;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final Session session : sessions)
			if (!session.getTutorial().isDraftMode() && !session.isDraftMode()) {
				param = String.format("id=%d", session.getId());
				super.request("/assistant/session/publish", param);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a session that wasn't registered by the principal,
		// HINT+ be it published or unpublished.

		final Collection<Session> sessions;
		String params;

		super.signIn("assistant2", "assistant2");
		sessions = this.repository.findManySessionsByAssistantUsername("assistant1");
		for (final Session session : sessions) {
			params = String.format("id=%d", session.getId());
			super.request("/assistant/session/publish", params);
		}
		super.signOut();
	}

}
