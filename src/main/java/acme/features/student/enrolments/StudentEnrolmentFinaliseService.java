
package acme.features.student.enrolments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Enrolment;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentFinaliseService extends AbstractService<Student, Enrolment> {
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

		super.bind(object, "cardLowerNibble", "cardHolder");
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;
		final SimpleDateFormat formatter;
		Integer cvv;
		final String expireDateString;
		final Date expireDate;

		cvv = super.getRequest().getData("cvv", Integer.class);
		expireDateString = super.getRequest().getData("expireDate", String.class);
		formatter = new SimpleDateFormat("MM/yyyy");

		super.state(object.getCardLowerNibble() != null && object.getCardLowerNibble().matches("^([0-9]{16})$"), "cardLowerNibble", "student.enrolment.form.error.invalid-card-number");
		super.state(!"".equals(object.getCardHolder()), "cardHolder", "student.enrolment.form.error.invalid-card-holder");
		super.state(cvv != null, "cvv", "student.enrolment.form.error.invalid-cvv");
		super.state(expireDateString != null && expireDateString.matches("^(0[1-9]|1[0-2])/(20)[2-9][0-9]$"), "expireDate", "student.enrolment.form.error.invalid-expireDate-format");

		if (expireDateString != null)
			try {
				expireDate = formatter.parse(expireDateString);
				super.state(!MomentHelper.isAfterOrEqual(MomentHelper.getCurrentMoment(), expireDate), "expireDate", "student.enrolment.form.error.invalid-expireDate-value");
			} catch (final ParseException e) {
				super.state(false, "expireDate", "student.enrolment.form.error.invalid-expireDate-format");
			}

		if (cvv != null)
			super.state(String.valueOf(cvv).length() == 3, "cvv", "student.enrolment.form.error.invalid-cvv");

		super.state(object.isDraftMode(), "draftMode", "student.enrolment.form.error.finalised");
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		object.setDraftMode(false);
		object.setCardLowerNibble(object.getCardLowerNibble().substring(12, 16));

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Double workTime;
		Integer cvv;
		String expireDate;

		expireDate = super.getRequest().hasData("expireDate") ? super.getRequest().getData("expireDate", String.class).trim() : null;
		cvv = super.getRequest().hasData("cvv") ? super.getRequest().getData("cvv", Integer.class) : null;
		workTime = this.repository.findWorktimeByEnrolmentId(object.getId());
		workTime = workTime != null ? workTime : 0.0;

		Tuple tuple;

		tuple = super.unbind(object, "cardLowerNibble", "cardHolder", "draftMode");
		tuple.put("readonly", false);
		tuple.put("cvv", cvv);
		tuple.put("expireDate", expireDate);

		super.getResponse().setData(tuple);
	}
}
