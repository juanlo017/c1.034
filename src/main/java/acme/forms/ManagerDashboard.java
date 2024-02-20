
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
	Integer						mustUserStories;
	Integer						shouldUserStories;
	Integer						couldUserStories;
	Integer						wontUserStories;

	Double						avgCostOfUserStory;
	Double						minCostOfUserStory;
	Double						maxCostOfUserStory;
	Double						deviationCostOfUserStory;

	Double						avgCostOfProject;
	Double						minCostOfProject;
	Double						maxCostOfProject;
	Double						deviationCostOfProject;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
