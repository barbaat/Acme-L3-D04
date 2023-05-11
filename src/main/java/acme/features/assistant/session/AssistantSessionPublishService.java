
package acme.features.assistant.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionPublishService extends AbstractService<Assistant, Session> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionRepository	repository;

	@Autowired
	protected SystemConfigurationService	scService;

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
		Assistant assistant;
		Tutorial tutorial;
		Session session;

		id = super.getRequest().getData("id", int.class);
		session = this.repository.findSessionById(id);
		tutorial = this.repository.findTutorialBySessionId(id);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = session != null && session.isDraftMode() && super.getRequest().getPrincipal().hasRole(assistant);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findSessionById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Session object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractTutorial", "goals", "estimatedTotalTime");
	}

	@Override
	public void validate(final Session object) {
		assert object != null;
	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractSession", "isTheorySession", "link", "draftMode");
		final String lang = super.getRequest().getLocale().getLanguage();
		tuple.put("initTimePeriod", this.scService.translateDate(object.getInitTimePeriod(), lang));
		tuple.put("finishTimePeriod", this.scService.translateDate(object.getFinishTimePeriod(), lang));
		tuple.put("isTheorySession", this.scService.translateBoolean(object.getIsTheorySession(), lang));
		tuple.put("masterId", object.getTutorial().getId());

		super.getResponse().setData(tuple);
	}

}
