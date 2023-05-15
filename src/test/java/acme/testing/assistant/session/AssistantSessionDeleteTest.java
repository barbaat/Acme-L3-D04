
package acme.testing.assistant.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.testing.TestHarness;

public class AssistantSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/session/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String code, final int sessionRecordIndex, final String title, final String abstractSession, final String isTheorySession, final String initTimePeriod, final String finishTimePeriod,
		final String link) {
		//Elimino una sesion correctamente

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkNotSubmitExists("Publish");
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("isTheorySession", isTheorySession);
		super.checkInputBoxHasValue("initTimePeriod", initTimePeriod);
		super.checkInputBoxHasValue("finishTimePeriod", finishTimePeriod);
		super.checkInputBoxHasValue("link", link);

		final String sessionIdString = super.getCurrentQuery();
		final int sessionId = Integer.parseInt(sessionIdString.substring(sessionIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", sessionId);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/assistant/session/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String abstractTutorial, final String goals, final String estimatedTotalTime, final String course) {
		//Compruebo que no puedo eliminar una session no publicada de un tutorial no publicado
		// Tambien que una sesion publicada de un tutorial no publicado
		// Y que una sesion publicada de un tutorial publicado

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "Tutorial List");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.checkColumnHasValue(recordIndex, 0, code);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractTutorial", abstractTutorial);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);

		final String sessionIdString = super.getCurrentQuery();
		final int sessionId = Integer.parseInt(sessionIdString.substring(sessionIdString.indexOf("=") + 1));
		final String param = String.format("id=%d", sessionId);

		super.checkNotSubmitExists("Delete");

		super.request("/assistant/session/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		//Compruebo que solo assistant1 puede eliminar sus sesiones

		final Collection<Tutorial> tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			final Collection<Session> sessions = this.repository.findManySessionsByTutorialId(tutorial.getId());
			for (final Session session : sessions)
				if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {

					final String param = String.format("id=%d", session.getId());

					super.checkLinkExists("Sign in");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();

					super.signIn("administrator", "administrator");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("assistant2", "assistant2");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("student1", "student1");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("lecturer1", "lecturer1");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("employer1", "employer1");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("auditor1", "auditor1");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();

					super.signIn("company1", "company1");
					super.request("/assistant/session/delete", param);
					super.checkPanicExists();
					super.signOut();
				}
		}
	}
}
