
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolments.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentPublishTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final String cardLowerNibble, final String cardHolder, final String cvv, final String expireDate) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);

		super.checkButtonExists("Payment form");
		super.clickOnButton("Payment form");

		super.checkFormExists();

		super.fillInputBoxIn("cardLowerNibble", cardLowerNibble);
		super.fillInputBoxIn("cardHolder", cardHolder);
		super.fillInputBoxIn("cvv", cvv);
		super.fillInputBoxIn("expireDate", expireDate);

		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();
		//super.checkInputBoxHasValue("cardLowerNibble", cardLowerNibble); Aquí hay los 4 últimos debido al perform del servicio
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("cardHolder", cardHolder);

		final Enrolment enrolment = this.repository.findEnrolmentByCode(code);
		final String params = String.format("id=%d", enrolment.getId());
		super.request("/student/enrolment/show", params);
		super.checkFormExists();
		super.checkNotButtonExists("Payment form");
		super.checkButtonExists("See workbook");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int enrolmentIndex, final String code, final String cardLowerNibble, final String cardHolder, final String cvv, final String expireDate) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);

		super.checkButtonExists("Payment form");
		super.clickOnButton("Payment form");

		super.checkFormExists();

		super.fillInputBoxIn("cardLowerNibble", cardLowerNibble);
		super.fillInputBoxIn("cardHolder", cardHolder);
		super.fillInputBoxIn("cvv", cvv);
		super.fillInputBoxIn("expireDate", expireDate);

		super.clickOnSubmit("Publish");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// Publicar enrolment con otro rol que no es Student

		Collection<Enrolment> enrolments;
		String params;

		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2");
		for (final Enrolment enrolment : enrolments)
			if (!enrolment.isDraftMode()) {
				params = String.format("id=%d", enrolment.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/enrolment/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/enrolment/register");
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/enrolment/register");
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/enrolment/register");
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/enrolment/register");
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// Publicar un enrolment que ya está publicado

		Collection<Enrolment> enrolments;
		String params;

		super.signIn("student1", "student1");
		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments)
			if (!enrolment.isDraftMode()) {
				params = String.format("id=%d", enrolment.getId());
				super.request("/student/enrolment/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		//Publicar un enrolment siendo Student pero no el que lo creó
		Collection<Enrolment> enrolments;
		String params;

		super.signIn("student2", "student2");
		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments) {
			params = String.format("id=%d", enrolment.getId());
			super.request("/student/enrolment/publish", params);
		}
		super.signOut();
	}

}
