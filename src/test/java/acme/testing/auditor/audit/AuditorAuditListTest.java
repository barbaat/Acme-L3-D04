
package acme.testing.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditorAuditListTest extends TestHarness {

	@Autowired
	protected AuditorAuditTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int auditIndex, final String code, final String conclusion) {
		//Compruebo que puedo visualizar correctamente un listado

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "List of audits");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(auditIndex, 0, code);
		super.checkColumnHasValue(auditIndex, 1, conclusion);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
	}

	@Test
	public void test300Hacking() {
		// Compruebo que si no eres auditor no puedes acceder al listado de audits

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/list");
		super.checkPanicExists();

		super.checkLinkExists("Sign in");
		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();

		super.checkLinkExists("Sign in");
		super.signIn("student1", "student1");
		super.request("/auditor/audit/list");
		super.checkPanicExists();
		super.signOut();
	}

	//	@Test
	//	public void test301Hacking() {
	//		// Compruebo que como auditor2 no puedo ver el listado de audits no publicadas del auditor1
	//
	//		final Collection<Audit> audits = this.repository.findAuditsByAuditorUsername("auditor1");
	//		for (final Audit audit : audits)
	//			if (audit.isDraftMode()) {
	//				final String param = String.format("id=%d", audit.getId());
	//
	//				super.checkLinkExists("Sign in");
	//				super.request("/auditor/audit/list", param);
	//				super.checkPanicExists();
	//
	//				super.signIn("auditor2", "auditor2");
	//				super.request("/auditor/audit/list", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//			}
	//	}
}
