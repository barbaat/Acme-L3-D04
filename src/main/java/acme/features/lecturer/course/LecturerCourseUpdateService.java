
package acme.features.lecturer.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.TypeCourse;
import acme.entities.lectures.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseUpdateService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repo;

	// AbstractService<Employer, Company> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repo.findOneCourseById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repo.findOneCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractCourse", "retailPrice", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		//		if (!super.getBuffer().getErrors().hasErrors("price"))
		//			super.state(this.auxiliarService.validatePrice(object.getPrice(), 0, 1000000), "price", "administrator.offer.form.error.price");
		//		if (!super.getBuffer().getErrors().hasErrors("title"))
		//			super.state(this.auxiliarService.validateTextImput(object.getTitle()), "title", "lecturer.course.form.error.spam");
		//		if (!super.getBuffer().getErrors().hasErrors("abstract$"))
		//			super.state(this.auxiliarService.validateTextImput(object.getAbstract$()), "abstract$", "lecturer.course.form.error.spam");
	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		this.repo.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "abstractCourse", "retailPrice", "link", "draftMode", "lecturer");
		final List<Lecture> lectures = this.repo.findManyLecturesByCourseId(object.getId()).stream().collect(Collectors.toList());
		final TypeCourse type = object.courseType(lectures);
		tuple.put("type", type);
		//tuple.put("money", this.auxiliarService.changeCurrency(object.getPrice()));
		super.getResponse().setData(tuple);
	}
}
