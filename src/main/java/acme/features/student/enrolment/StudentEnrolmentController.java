
package acme.features.student.enrolment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.enrolments.Enrolment;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentEnrolmentController extends AbstractController<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentListService			listService;

	@Autowired
	protected StudentEnrolmentShowService			showService;

	@Autowired
	protected StudentEnrolmentUpdateService			updateService;

	@Autowired
	protected StudentEnrolmentDeleteService			deleteService;

	@Autowired
	protected StudentEnrolmentRegisterService		registerService;

	@Autowired
	protected StudentEnrolmentShowWorkbookService	workbookService;

	@Autowired
	protected StudentEnrolmentFinaliseService		finaliseService;
	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("register", "create", this.registerService);
		super.addCustomCommand("show-workbook", "show", this.workbookService);
		super.addCustomCommand("publish", "update", this.finaliseService);
	}

}
