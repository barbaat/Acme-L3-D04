/*
 * EmployerJobListMineTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureListAllTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/list-all-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractLecture, final String estimatedLearningTimeInHours) {
		// Aquí se listan todos las lecciones de un profesor

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List of lectures");
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
		// Nadie a parte del profesor, puede ver su lista de lecciones
		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/list-all");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/list-all");
		super.checkPanicExists();

		super.signOut();
	}

}
