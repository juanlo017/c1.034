
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							totalNumberOfSponsorshipsWithLink;
	int							totalNumberOfInvoicesWithLessOrEqual21Tax;

	double						avgAmountOfSponsorships;
	double						deviationAmountOfSponsorships;
	double						minAmountOfSponsorships;
	double						maxAmountOfSponsorships;

	double						avgQuantityOfInvoices;
	double						deviationQuantityOfInvoices;
	double						minQuantityOfInvoices;
	double						maxQuantityOfInvoices;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
