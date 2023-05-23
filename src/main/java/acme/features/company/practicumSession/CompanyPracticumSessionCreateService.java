
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository psRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("masterId", int.class);
		super.getResponse().setChecked(status);

	}

	@Override
	public void authorise() {
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum object = this.psRepository.findPracticumById(masterId);
		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(object.getCompany().getUserAccount().getId() == userAccountId);

	}

	@Override
	public void load() {

		PracticumSession object;

		object = new PracticumSession();
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		final Boolean exc = !practicum.isDraftMode();
		object.setExceptional(exc);
		object.setPracticum(practicum);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;
		super.bind(object, "title", "abstract$", "optionalLink", "startPeriod", "finishPeriod", "confirmation");
	}

	@Override
	public void validate(final PracticumSession object) {

		//Sesión exceptional única
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		Collection<PracticumSession> exceptionals;
		exceptionals = this.psRepository.findExceptionalSessionsByPracticumId(masterId);

		if (!practicum.isDraftMode()) {
			final boolean valid = exceptionals.size() == 0;
			super.state(valid, "*", "company.practicum-session.validation.practicum.error.ExceptionalAlreadyExists");
		}

		//Confirmación para la sesiones de práctica únicas
		final boolean confirmation = object.getPracticum().isDraftMode() ? true : super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "company.practicum-session.form.label.confirmation");

		//Validaciones de la fecha de comienzo y de la fecha de finalización
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("startPeriod")) {
			Date minStartPeriod;
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartPeriod(), minStartPeriod), "startPeriod", "company.practicum-session.validation.startPeriod.error.WeekAhead");
		}

		if (!super.getBuffer().getErrors().hasErrors("finishPeriod") && !super.getBuffer().getErrors().hasErrors("startPeriod")) {
			Date minFinishPeriod;
			minFinishPeriod = MomentHelper.deltaFromMoment(object.getStartPeriod(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getFinishPeriod(), minFinishPeriod), "finishPeriod", "company.practicum-session.validation.finishPeriod.error.WeekLong");
		}

	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.psRepository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {

		assert object != null;

		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		final Tuple tuple = super.unbind(object, "title", "abstract$", "optionalLink", "startPeriod", "finishPeriod", "exceptional");
		tuple.put("masterId", masterId);
		tuple.put("draftMode", practicum.isDraftMode());
		tuple.put("confirmation", false);
		super.getResponse().setData(tuple);
	}

}
