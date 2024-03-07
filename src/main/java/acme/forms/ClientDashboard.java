
package acme.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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

	@PositiveOrZero
	int							avgContractBudget;

	@PositiveOrZero
	int							minContractBudget;

	@PositiveOrZero
	int							maxContractBudget;

	@PositiveOrZero
	int							deviationContractBudget;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
