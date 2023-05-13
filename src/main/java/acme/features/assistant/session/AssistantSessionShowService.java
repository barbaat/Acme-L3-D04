
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
public class AssistantSessionShowService extends AbstractService<Assistant, Session> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantSessionRepository	repository;

	@Autowired
	protected SystemConfigurationService	scService;

	// AbstractService<Assistant, Session> ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int sessionId;
		Tutorial tutorial;

		sessionId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialBySessionId(sessionId);
		status = tutorial != null && (super.getRequest().getPrincipal().hasRole(tutorial.getAssistant()) || !tutorial.isDraftMode());

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
	public void unbind(final Session object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractSession", "isTheorySession", "link", "draftMode");
		final String lang = super.getRequest().getLocale().getLanguage();
		tuple.put("initTimePeriod", this.scService.translateDate(object.getInitTimePeriod(), lang));
		tuple.put("finishTimePeriod", this.scService.translateDate(object.getFinishTimePeriod(), lang));
		tuple.put("tutorialId", object.getTutorial().getId());

		super.getResponse().setData(tuple);
	}

}
