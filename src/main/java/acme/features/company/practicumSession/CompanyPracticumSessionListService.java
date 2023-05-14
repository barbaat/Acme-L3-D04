
package acme.features.company.practicumSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionListService extends AbstractService<Company, PracticumSession> {

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
		boolean status;
		final int masterId;
		final Practicum practicum;

		masterId = super.getRequest().getData("masterId", int.class);
		practicum = this.psRepository.findPracticumById(masterId);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		status = practicum.getCompany().getUserAccount().getId() == userAccountId;
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<PracticumSession> object;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		object = this.psRepository.findPracticumSessionsByPracticumId(masterId);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		final boolean exceptional = object.isExceptional();
		String res = "";
		if (exceptional == true)
			res = "*";
		else
			res = " ";
		final Tuple tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod");
		tuple.put("draftMode", practicum.isDraftMode());
		tuple.put("exceptional", res);
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<PracticumSession> object) {
		//		assert object != null;
		//
		//		Tuple tuple;
		//
		//		tuple = super.unbind(object, "title", "abstract$", "startPeriod", "finishPeriod", "draftMode", "exceptional");
		//
		//		super.getResponse().setData(tuple);

		assert object != null;
		boolean createButton = false;
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Practicum practicum = this.psRepository.findPracticumById(masterId);
		if (super.getRequest().getPrincipal().getAccountId() == practicum.getCompany().getUserAccount().getId())
			createButton = true;
		super.getResponse().setGlobal("createButton", createButton);
		super.getResponse().setGlobal("draftMode", practicum.isDraftMode());
		super.getResponse().setGlobal("masterId", masterId);

	}
}
