
package acme.features.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.practicums.Practicum;
import acme.entities.sessions.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionShowService extends AbstractService<Company, PracticumSession> {

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
		boolean status;
		int pId;
		final Practicum p;

		pId = super.getRequest().getData("id", int.class);
		p = this.psRepository.findPracticumByPracticumSessionId(pId);

		final int userAccountId = super.getRequest().getPrincipal().getAccountId();
		status = p.getCompany().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);

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
	public void unbind(final PracticumSession object) {

		assert object != null;

		final int id = super.getRequest().getData("id", int.class);
		final Practicum practicum = this.psRepository.findPracticumByPracticumSessionId(id);
		final Tuple tuple = super.unbind(object, "title", "abstract$", "optionalLink", "exceptional");
		final String lang = super.getRequest().getLocale().getLanguage();
		tuple.put("startPeriod", this.scService.translateDate(object.getStartPeriod(), lang));
		tuple.put("finishPeriod", this.scService.translateDate(object.getFinishPeriod(), lang));
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.isDraftMode());

		super.getResponse().setData(tuple);
	}
}
