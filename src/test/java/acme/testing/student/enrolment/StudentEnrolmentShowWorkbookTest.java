
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolments.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentShowWorkbookTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentTestRepository repository;

	// Test methods -----------------------------------------------------------


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/show-workbook-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final String courseTitle, final String workTime, final String numActivities) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "List of enrolments");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.clickOnListingRecord(enrolmentIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("See workbook");
		super.checkFormExists();

		super.checkInputBoxHasValue("courseTitle", courseTitle);
		super.checkInputBoxHasValue("workTime", workTime);
		super.checkInputBoxHasValue("numActivities", numActivities);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		//No se hacen
	}
	@Test
	public void test300Hacking() {
		Collection<Enrolment> enrolments;
		String param;

		super.signIn("student1", "student1");
		enrolments = this.repository.findManyEnrolmentsByStudentUsername("student2");
		for (final Enrolment enrolment : enrolments)
			if (!enrolment.isDraftMode()) {
				param = String.format("id=%d", enrolment.getId());

				super.request("/student/enrolment/show-workbook", param);
				super.checkPanicExists();
			}
	}

}
