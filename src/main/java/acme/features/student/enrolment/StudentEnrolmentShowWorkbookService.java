
package acme.features.student.enrolment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentShowWorkbookService extends AbstractService<Student, Enrolment> {
	//Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("enrolmentId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int enrolmentId;
		Enrolment enrolment;
		Principal principal;
		Student student;

		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		principal = super.getRequest().getPrincipal();
		student = this.repository.findStudentById(principal.getActiveRoleId());
		status = student != null && enrolment.getStudent().equals(student);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("enrolmentId", int.class);
		object = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Double workTime;
		Integer numActivities;

		workTime = this.repository.findWorktimeByEnrolmentId(object.getId());
		workTime = workTime != null ? workTime : 0.0;
		numActivities = this.repository.findNumberActivitiesByEnrolmentId(object.getId());

		Tuple tuple;

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode");
		tuple.put("readonly", true);
		tuple.put("workTime", workTime);
		tuple.put("numActivities", numActivities);
		tuple.put("courseTitle", object.getCourse().getTitle());

		super.getResponse().setData(tuple);
	}
}
