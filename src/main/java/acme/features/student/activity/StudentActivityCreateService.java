
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.activities.ActivityType;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

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
		student = this.repository.findStudentByPrincipalId(principal.getActiveRoleId());
		status = student != null && enrolment.getStudent().equals(student) && !enrolment.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int enrolmentId;
		Enrolment enrolment;

		object = new Activity();

		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);

		object.setEnrolment(enrolment);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, "title", "abstractt", "activityType", "link", "startPeriod", "endPeriod");
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("endPeriod") && !super.getBuffer().getErrors().hasErrors("startPeriod")) {

			super.state(MomentHelper.isAfter(object.getEndPeriod(), object.getStartPeriod()), "endPeriod", "student.activity.form.error.endPeriod-too-soon");
			super.state(MomentHelper.isBeforeOrEqual(object.getEndPeriod(), MomentHelper.parse("yyyy-MM-dd-HH:mm", "2099-12-31-23:59")), "endPeriod", "student.activity.form.error.endPeriod-too-late");
			super.state(MomentHelper.isAfterOrEqual(object.getStartPeriod(), MomentHelper.parse("yyyy-MM-dd-HH:mm", "2022-01-01-00:00")), "startPeriod", "student.activity.form.error.startPeriod-too-soon");
		}
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		int enrolmentId;

		final SelectChoices typesChoices;
		Tuple tuple;

		typesChoices = SelectChoices.from(ActivityType.class, object.getActivityType());
		enrolmentId = super.getRequest().getData("enrolmentId", int.class);

		tuple = super.unbind(object, "title", "abstractt", "activityType", "link", "startPeriod", "endPeriod");
		tuple.put("types", typesChoices);
		tuple.put("readonly", object.getEnrolment().isDraftMode());
		super.getResponse().setGlobal("enrolmentId", enrolmentId);

		super.getResponse().setData(tuple);
	}
}
