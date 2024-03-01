
package acme.forms;

import java.util.Date;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	int							totalNumberOfTrainingModules;
	int							totalNumberOfTrainingSessions;
	Date						lastUpdateMoment;

	double						avgTimeOfTrainingModule;
	double						minTimeOfTrainingModule;
	double						maxTimeOfTrainingModule;
	double						deviationTimeOfTrainingModule;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
