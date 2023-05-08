///*
// * EmployerDutyListTest.java
// *
// * Copyright (C) 2012-2023 Rafael Corchuelo.
// *
// * In keeping with the traditional purpose of furthering education and research, it is
// * the policy of the copyright owner to permit non-commercial use and redistribution of
// * this software. It has been tested carefully, but it is not guaranteed for any particular
// * purposes. The copyright owner does not offer any warranties or representations, nor do
// * they accept any liabilities with respect to them.
// */
//
//package acme.testing.lecturer.course;
//
//import java.util.Collection;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.entities.jobs.Job;
//import acme.testing.TestHarness;
//
//public class LecturerCourseListTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected LecturerCourseTestRepository repository;
//
//	// Test methods -----------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/employer/duty/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int jobRecordIndex, final String reference, final int dutyRecordIndex, final String title, final String workLoad) {
//		// HINT: this test authenticates as an employer, then lists his or her jobs, 
//		// HINT+ selects one of them, and check that it has the expected duties.
//
//		super.signIn("employer1", "employer1");
//
//		super.clickOnMenu("Employer", "List my jobs");
//		super.checkListingExists();
//		super.sortListing(0, "asc");
//
//		super.checkColumnHasValue(jobRecordIndex, 0, reference);
//		super.clickOnListingRecord(jobRecordIndex);
//		super.checkInputBoxHasValue("reference", reference);
//		super.clickOnButton("Duties");
//
//		super.checkListingExists();
//		super.checkColumnHasValue(dutyRecordIndex, 0, title);
//		super.checkColumnHasValue(dutyRecordIndex, 1, workLoad);
//		super.clickOnListingRecord(dutyRecordIndex);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there's no negative test case for this listing, since it doesn't
//		// HINT+ involve filling in any forms.
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to list the duties of a job that is unpublished
//		// HINT+ using a principal that didn't create it. 
//
//		Collection<Job>	jobs;
//		String			param;
//
//		jobs = this.repository.findManyJobsByEmployerUsername("employer1");
//		for (final Job job : jobs)
//			if (job.isDraftMode()) {
//				param = String.format("masterId=%d", job.getId());
//
//				super.checkLinkExists("Sign in");
//				super.request("/employer/duty/list", param);
//				super.checkPanicExists();
//
//				super.signIn("administrator", "administrator");
//				super.request("/employer/duty/list", param);
//				super.checkPanicExists();
//				super.signOut();
//
//				super.signIn("employer2", "employer2");
//				super.request("/employer/duty/list", param);
//				super.checkPanicExists();
//				super.signOut();
//
//				super.signIn("worker1", "worker1");
//				super.request("/employer/duty/list", param);
//				super.checkPanicExists();
//				super.signOut();
//			}
//	}
//
//}
