
package acme.features.sponsor.invoices;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.sponsorships.Invoices;
import acme.roles.Sponsor;

@Controller
public class SponsorInvoicesController extends AbstractController<Sponsor, Invoices> {

	@Autowired
	private SponsorInvoicesCreateService	createService;

	@Autowired
	private SponsorInvoicesDeleteService	deleteService;

	@Autowired
	private SponsorInvoicesListMineService	listMineService;

	@Autowired
	private SponsorInvoicesPublishService	publishService;

	@Autowired
	private SponsorInvoicesShowService		showService;

	@Autowired
	private SponsorInvoicesUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("show", this.showService);
	}
}
