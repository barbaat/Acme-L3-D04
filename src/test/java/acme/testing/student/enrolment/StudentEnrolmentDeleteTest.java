
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolments.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentDeleteTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final String course, final String motivation, final String goals) {
		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		//super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);

		final Enrolment enrolment = this.repository.findEnrolmentByCode(code);
		final String param = String.format("id=%d", enrolment.getId());
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.request("/student/enrolment/delete", param);
		super.checkPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int enrolmentIndex, final String code, final String course, final String motivation, final String goals) {
		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);

		super.checkNotButtonExists("Delete");

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments) {
			param = String.format("id=%d", enrolment.getId());

			super.checkLinkExists("Sign in");
			super.request("/student/enrolment/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/student/enrolment/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/student/enrolment/list");
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/student/enrolment/list");
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/student/enrolment/list");
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/student/enrolment/list");
			super.checkPanicExists();
			super.signOut();

		}
	}
}
