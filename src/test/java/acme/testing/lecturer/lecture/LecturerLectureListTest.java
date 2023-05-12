
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.courses.Course;
import acme.testing.TestHarness;

public class LecturerLectureListTest extends TestHarness {

	@Autowired
	protected LecturerLectureTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/list-course-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final int lectureIndex, final String code, final String title, final String abstractLecture, final String estimatedLearningTimeInHours) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, code);
		super.clickOnListingRecord(courseIndex);

		super.checkInputBoxHasValue("code", code);

		super.checkButtonExists("Lectures of course");
		super.clickOnButton("Lectures of course");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(lectureIndex, 0, title);
		super.checkColumnHasValue(lectureIndex, 1, abstractLecture);
		super.checkColumnHasValue(lectureIndex, 2, estimatedLearningTimeInHours);

		super.clickOnListingRecord(lectureIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// No puede haber
	}

	@Test
	public void test300Hacking() {

		Collection<Course> cursos;
		String params;

		cursos = this.repository.findManyCoursesByLecturerUsername("lecturer2");
		for (final Course curso : cursos) {
			params = String.format("masterId=%d", curso.getId());

			super.checkLinkExists("Sign in");
			super.request("/lecturer/lecture/list", params);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/lecturer/lecture/list", params);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
