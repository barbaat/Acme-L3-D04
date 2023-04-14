
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.entities.tutorials.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

public class AssistantTutorialUpdateService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

	// AbstractService<Assistant, Tutorial> ----------------------------------------------


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
		Tutorial tutorial;
		Assistant assistant;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(id);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = super.getRequest().getPrincipal().hasRole(assistant) || tutorial != null && !tutorial.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);

		super.getBuffer().setData(object);

	}

	@Override
<<<<<<< Upstream, based on 1310c4f0e810dcb97b2c27b92463b787b7af16d8
	public void bind(final Tutorial object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		super.bind(object, "code", "title", "abstractTutorial", "goals", "estimatedTotalTime");
		object.setCourse(course);
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "title", "abstractTutorial", "goals", "estimatedTotalTime", "draftMode");
<<<<<<< Upstream, based on 1310c4f0e810dcb97b2c27b92463b787b7af16d8
=======
	public void unbind(final Tutorial object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "abstractTutorial", "goals", "estimatedTotalTime");
>>>>>>> 7268a1e Task-111: first part almost done
=======
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
>>>>>>> b0b97c5 Task 111: updated

		super.getResponse().setData(tuple);
	}

}
