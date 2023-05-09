
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activities.Activity;
import acme.entities.activities.ActivityType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityUpdateService extends AbstractService<Student, Activity> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

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
		Activity activity;
		final Principal principal;
		Student student;

		id = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(id);
		principal = super.getRequest().getPrincipal();
		student = this.repository.findStudentByPrincipalId(principal.getActiveRoleId());
		status = student != null && activity.getEnrolment().getStudent().equals(student) && !activity.getEnrolment().isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		Activity object;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findActivityById(id);

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

		final SelectChoices typesChoices;
		Tuple tuple;

		typesChoices = SelectChoices.from(ActivityType.class, object.getActivityType());

		tuple = super.unbind(object, "title", "abstractt", "activityType", "link", "startPeriod", "endPeriod");
		tuple.put("types", typesChoices);
		tuple.put("readonly", object.getEnrolment().isDraftMode());

		super.getResponse().setData(tuple);
	}
}
