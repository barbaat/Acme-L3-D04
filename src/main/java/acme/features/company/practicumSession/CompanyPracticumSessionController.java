
package acme.features.company.practicumSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.sessions.PracticumSession;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanyPracticumSessionController extends AbstractController<Company, PracticumSession> {

	@Autowired
	protected CompanyPracticumSessionCreateService	createService;

	@Autowired
	protected CompanyPracticumSessionListService	listService;

	@Autowired
	protected CompanyPracticumSessionShowService	showService;

	@Autowired
	protected CompanyPracticumSessionUpdateService	updateService;

	@Autowired
	protected CompanyPracticumSessionDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
