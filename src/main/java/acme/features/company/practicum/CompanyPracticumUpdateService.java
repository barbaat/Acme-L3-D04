
package acme.features.company.practicum;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.courses.Course;
import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumUpdateService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository practicumRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Practicum object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.practicumRepository.findPracticumById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getCompany().getUserAccount().getId() == userAccountId && object.isDraftMode());

	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.practicumRepository.findPracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.practicumRepository.findCourseById(courseId);

		super.bind(object, "code", "title", "abstract$", "goals", "estimatedTotalTime");
		object.setCourse(course);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		int practicumId;
		final Collection<Course> courses = this.practicumRepository.findPublishedCourses();

		practicumId = super.getRequest().getData("id", int.class);
		if (!super.getBuffer().getErrors().hasErrors("estimatedTotalTime")) {
			final Collection<PracticumSession> sessions = this.practicumRepository.findPracticumSessionsByPracticumId(practicumId);
			final Double estimatedTotalTime = object.getEstimatedTotalTime();
			double totalHours = 0.0;

			for (final PracticumSession session : sessions) {
				final Date start = session.getStartPeriod();
				final Date finish = session.getFinishPeriod();
				final Duration duration = MomentHelper.computeDuration(start, finish);
				totalHours += duration.toHours();
			}
			final boolean comprobar = totalHours < estimatedTotalTime * 0.9 || totalHours > estimatedTotalTime * 1.1;
			super.state(!comprobar, "estimatedTotalTime", "company.practicum.form.error.estimated-total-time");
		}

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.practicumRepository.findPracticumByCode(object.getCode()) == null || this.practicumRepository.findPracticumByCode(object.getCode()).equals(object), "code", "company.practicum.form.error.existing-code");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(courses.contains(object.getCourse()), "course", "Error al seleccionar un curso no publicado");

	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.practicumRepository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.practicumRepository.findPublishedCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "title", "abstract$", "goals", "estimatedTotalTime", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}
}
