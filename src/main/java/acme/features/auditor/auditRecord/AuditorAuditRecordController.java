
package acme.features.auditor.auditRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.auditRecords.AuditRecord;
import acme.roles.Auditor;

@Controller
public class AuditorAuditRecordController extends AbstractController<Auditor, AuditRecord> {

	// Internal state -----------------------------------------------------
	@Autowired
	private AuditorAuditRecordShowService		showService;

	@Autowired
	private AuditorAuditRecordListMineService	listMineService;

	@Autowired
	private AuditorAuditRecordCreateService		createService;

	@Autowired
	private AuditorAuditRecordUpdateService		updateService;

	@Autowired
	private AuditorAuditRecordDeleteService		deleteService;


	// Constructors ---------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-mine", "list", this.listMineService);

	}

}
