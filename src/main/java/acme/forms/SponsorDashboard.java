
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	double						totalNumInvoicesWithTaxLessOrEqualTo21;
	double						totalNumInvoicesWithLink;

	// sponsorships
	Map<String, Double>			averageSponsorshipsAmount;
	Map<String, Double>			deviationSponsorshipsAmount;
	Map<String, Double>			minimumSponsorshipsAmount;
	Map<String, Double>			maximumSponsorshipsAmount;

	//invoices
	Map<String, Double>			averageInvoicesQuantity;
	Map<String, Double>			deviationInvoicesQuantity;
	Map<String, Double>			minimumInvoicesQuantity;
	Map<String, Double>			maximumInvoicesQuantity;

	String[]					supportedCurrencies;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
