
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	int							mustUserStories;
	int							shouldUserStories;
	int							couldUserStories;
	int							wontUserStories;

	double						avgCostOfUserStory;
	double						minCostOfUserStory;
	double						maxCostOfUserStory;
	double						deviationCostOfUserStory;

	double						avgCostOfProjectEUR;
	double						avgCostOfProjectUSD;
	double						avgCostOfProjectGBP;

	double						minCostOfProjectEUR;
	double						minCostOfProjectUSD;
	double						minCostOfProjectGBP;

	double						maxCostOfProjectEUR;
	double						maxCostOfProjectUSD;
	double						maxCostOfProjectGBP;

	double						deviationCostOfProjectEUR;
	double						deviationCostOfProjectUSD;
	double						deviationCostOfProjectGBP;

}
