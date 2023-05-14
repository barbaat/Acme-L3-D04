
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionUpdateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository	psRepository;

	@Autowired
	protected SystemConfigurationService		scService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {

		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		//		boolean status;
		//		int sessionId;
		//		PracticumSession session;
		//		Company company;
		//
		//		sessionId = super.getRequest().getData("id", int.class);
		//		session = this.psRepository.findPracticumSessionById(sessionId);
		//		company = session == null ? null : session.getPracticum().getCompany();
		//		status = session != null && session.isDraftMode() && super.getRequest().getPrincipal().hasRole(company);
		//
		//		super.getResponse().setAuthorised(status);

		int pId;
		Practicum practicum;

		pId = super.getRequest().getData("id", int.class);
		practicum = this.psRepository.findPracticumByPracticumSessionId(pId);
		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(practicum.getCompany().getUserAccount().getId() == userAccountId && practicum.isDraftMode());

	}

	@Override
	public void load() {

		PracticumSession object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.psRepository.findPracticumSessionById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		//		assert object != null;
		//
		//		int practicumId;
		//		Practicum practicum;
		//
		//		practicumId = super.getRequest().getData("practicum", int.class);
		//		practicum = this.psRepository.findPracticumById(practicumId);
		//
		//		super.bind(object, "title", "abstract$", "startPeriod", "finishPeriod", "optionalLink");
		//
		//		object.setPracticum(practicum);

		assert object != null;
		super.bind(object, "title", "abstract$", "startPeriod", "finishPeriod", "optionalLink");

	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("startPeriod")) {
			Date minStartPeriod;
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartPeriod(), minStartPeriod), "startPeriod", "company.practicum-session.validation.startPeriod.error.WeekAhead");
		}

		if (!super.getBuffer().getErrors().hasErrors("finishPeriod")) {
			Date minFinishPeriod;
			minFinishPeriod = MomentHelper.deltaFromMoment(object.getStartPeriod(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getFinishPeriod(), minFinishPeriod), "finishPeriod", "company.practicum-session.validation.finishPeriod.error.WeekLong");
		}

		//Practicum Validation: si quito esto, el error de null pointer exception al poner "---" se quita, pero sí puedo seleccionar una practica publicada por lo que eso no se puede.
		//		final Collection<Practicum> practica;
		//		final SelectChoices choices;
		//		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();
		//
		//		practica = this.psRepository.findManyPrivatePracticaByCompanyId(companyId);
		//		choices = SelectChoices.from(practica, "code", object.getPracticum());
		//
		//		final int selectedId = Integer.parseInt(choices.getSelected().getKey());
		//		final Practicum selectedPracticum = this.psRepository.findPracticumById(selectedId);
		//
		//		final boolean valid = selectedPracticum.isDraftMode();
		//		super.state(valid, "practicum", "company.practicum-session.validation.practicum.error.Published");

	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.psRepository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		//		assert object != null;
		//		final Collection<Practicum> practica;
		//		final SelectChoices choices;
		//		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();
		//
		//		practica = this.psRepository.findManyPrivatePracticaByCompanyId(companyId);
		//		choices = SelectChoices.from(practica, "code", object.getPracticum());
		//		Tuple tuple;
		//
		//		tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod", "draftMode", "exceptional", "optionalLink");
		//		tuple.put("practicum", choices.getSelected().getKey());
		//		tuple.put("practica", choices);
		//
		//		super.getResponse().setData(tuple);

		assert object != null;
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		final Tuple tuple = super.unbind(object, "title", "abstract$", "optionalLink");
		final String lang = super.getRequest().getLocale().getLanguage();
		tuple.put("startPeriod", this.scService.translateDate(object.getStartPeriod(), lang));
		tuple.put("finishPeriod", this.scService.translateDate(object.getFinishPeriod(), lang));
		tuple.put("masterId", masterId);
		tuple.put("draftMode", practicum.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
