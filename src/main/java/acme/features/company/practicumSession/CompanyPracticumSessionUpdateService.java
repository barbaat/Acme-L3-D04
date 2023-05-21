
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionUpdateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository psRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {

		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		final int id = super.getRequest().getData("id", int.class);
		final PracticumSession object = this.psRepository.findPracticumSessionById(id);
		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		super.getResponse().setAuthorised(object.getPracticum().getCompany().getUserAccount().getId() == userAccountId && object.getPracticum().isDraftMode());

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

		final Tuple tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod", "optionalLink");
		tuple.put("id", object.getId());
		tuple.put("draftMode", object.getPracticum().isDraftMode());
		super.getResponse().setData(tuple);

	}
}
