
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int id;
		Enrolment enrolment;
		final Principal principal;
		Student student;

		id = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findEnrolmentById(id);
		principal = super.getRequest().getPrincipal();
		student = this.repository.findStudentById(principal.getActiveRoleId());
		status = student != null && enrolment.getStudent().equals(student) && enrolment.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Enrolment object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		object.setCourse(course);

		super.bind(object, "code", "motivation", "goals");
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;
		final Enrolment enrolmentWithSameCode = this.repository.findEnrolmentByCode(object.getCode());
		super.state(enrolmentWithSameCode == null || enrolmentWithSameCode.getId() == object.getId(), "code", "student.enrolment.form.error.duplicated-code");
		super.state(object.isDraftMode(), "draftMode", "student.enrolment.form.error.finalised");
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Double workTime;
		Collection<Course> courses;
		SelectChoices choices;

		Tuple tuple;
		courses = this.repository.findAllPublishedCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		workTime = this.repository.findWorktimeByEnrolmentId(object.getId());
		workTime = workTime != null ? workTime : 0.0;

		tuple = super.unbind(object, "code", "motivation", "goals", "draftMode", "cardLowerNibble", "cardHolder");
		tuple.put("readonly", !object.isDraftMode());
		tuple.put("workTime", workTime);
		tuple.put("courseTitle", object.getCourse().getTitle());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}
}
