
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.lectures.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureListService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repo.findManyLecturesByCourseId(masterId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractLecture", "body", "estimatedLearningTimeInHours", "lectureType", "link", "published");
		tuple.put("publishedMode", object.isPublished());
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Lecture> objects) {
		assert objects != null;

		int masterId;
		Course course;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		course = this.repo.findOneCourseById(masterId);
		showCreate = course.isPublished() && super.getRequest().getPrincipal().hasRole(course.getLecturer());

		super.getResponse().setGlobal("masterId", masterId);
		super.getResponse().setGlobal("showCreate", showCreate);
	}

}
