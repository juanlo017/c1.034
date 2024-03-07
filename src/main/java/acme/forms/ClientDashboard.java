
package acme.forms;

import javax.validation.constraints.NotNull;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							numberOfProgressLogs;
	@NotNull
	Percentil					progressLogsCompletenessRate;

	double						avgContractBudget;
	double						minContractBudget;
	double						maxContractBudget;
	double						deviationContractBudget;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
