
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audits.Audit;
import acme.testing.TestHarness;

public class AuditorAuditShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints, final String mark) {
		//Compruebo que puedo visualizar un audit correctamente

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);
		super.checkInputBoxHasValue("mark", mark);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		//Compruebo que solo auditor1 pueda ver sus audits sin publicar

		super.signIn("auditor1", "auditor1");
		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				final String param = String.format("id=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/audit/show", param);
				super.checkPanicExists();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/audit/show", param);
				super.checkPanicExists();
				super.signOut();

				super.checkLinkExists("Sign in");
				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/audit/show", param);
				super.checkPanicExists();
				super.signOut();

				super.checkLinkExists("Sign in");
				super.signIn("student1", "student1");
				super.request("/auditor/audit/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
