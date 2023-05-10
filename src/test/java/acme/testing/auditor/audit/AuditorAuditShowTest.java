
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
		// HINT: this test tries to show a duty of a job that is in draft mode or
		// HINT+ not available, but wasn't published by the principal;

		Collection<Audit> audits;
		String param;

		super.signIn("auditor1", "auditor1");
		final int auditorId = this.repository.findAuditorByUsername("user-account-auditor2").getId();

		audits = this.repository.findAuditsByAuditorId(auditorId);
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				param = String.format("id=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/audit/show", param);
				super.checkPanicExists();
			}
	}

}
