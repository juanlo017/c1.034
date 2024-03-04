
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							numOfAuditors;
	int							numOfConsumers;
	int							numOfDevelopers;
	int							numOfManagers;
	int							numOfProviders;
	int							numOfSponsors;
	int							numOfClients; //still have to close clients role pr

	double						ratioNonCriticalObjectives;
	double						ratioNoticesWithMailAndLink;

	double						avgRiskValue;
	double						minRiskValue;
	double						maxRiskValue;
	double						deviationRiskValue;

	double						avgClaimsPostedLast10Weeks;
	double						minClaimsPostedLast10Weeks;
	double						maxClaimsPostedLast10Weeks;
	double						deviationClaimsPostedLast10Weeks;

}
