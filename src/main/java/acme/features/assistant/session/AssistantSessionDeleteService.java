/*
 * AssistantTutorialDeleteService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.sessions.Session;
import acme.entities.tutorials.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantSessionDeleteService extends AbstractService<Assistant, Session> {

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
		final int sessionId;
		Assistant assistant;
		Session session;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findSessionById(sessionId);
		assistant = session == null ? null : session.getTutorial().getAssistant();
		status = session != null && session.getTutorial().isDraftMode() && super.getRequest().getPrincipal().hasRole(assistant);

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

		int tutorialId;
		Tutorial tutorial;
		tutorialId = super.getRequest().getData("tutorial", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);

		super.bind(object, "title", "abstractSession", "isTheorySession", "initTimePeriod", "finishTimePeriod", "link");
		object.setTutorial(tutorial);
	}

	@Override
	public void validate(final Session object) {
		assert object != null;
	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		Tuple tuple;

		Collection<Tutorial> tutorials;
		SelectChoices choices;

		tutorials = this.repository.findAllTutorials();
		choices = SelectChoices.from(tutorials, "code", object.getTutorial());

		tuple = super.unbind(object, "title", "abstractSession", "link");
		tuple.put("tutorial", choices.getSelected().getKey());
		tuple.put("tutorials", choices);
		tuple.put("tutorialId", object.getTutorial().getId());
		tuple.put("draftMode", object.getTutorial().isDraftMode());

		super.getResponse().setData(tuple);
	}

}
