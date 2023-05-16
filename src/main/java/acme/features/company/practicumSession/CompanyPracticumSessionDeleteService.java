
package acme.features.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionDeleteService extends AbstractService<Company, PracticumSession> {

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
		assert object != null;
		super.bind(object, "title", "abstract$", "startPeriod", "finishPeriod", "optionalLink");

		//		assert object != null;
		//
		//		int practicumId;
		//		Practicum practicum;
		//
		//		practicumId = super.getRequest().getData("practicum", int.class);
		//		practicum = this.psRepository.findPracticumById(practicumId);
		//
		//		super.bind(object, "title", "abstract$", "optionalLink", "startPeriod", "finishPeriod");
		//
		//		object.setPracticum(practicum);

	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;
	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.psRepository.delete(object);
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
		//		tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod", "draftMode", "exceptional", "optionalLink", "estimatedTotalTime");
		//		tuple.put("practicum", choices.getSelected().getKey());
		//		tuple.put("practica", choices);
		//
		//		super.getResponse().setData(tuple);

		assert object != null;
		final Tuple tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod", "optionalLink");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().setData(tuple);

	}
}
