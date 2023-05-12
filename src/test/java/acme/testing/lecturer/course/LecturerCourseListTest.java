
package acme.testing.lecturer.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class LecturerCourseListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final String code, final String title, final String retailPrice) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.checkColumnHasValue(courseIndex, 1, title);
		super.checkColumnHasValue(courseIndex, 2, retailPrice);

		super.clickOnListingRecord(courseIndex);
		super.signOut();
	}

	@Test
	public void test200Negative() {
		// No puede haber		
	}

	@Test
	public void test300Hacking() {
		// Preguntar al profesor
	}

}
