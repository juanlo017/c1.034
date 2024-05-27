
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
	Double						avgContractBudgetEUR;

	@PositiveOrZero
	Double						minContractBudgetEUR;

	@PositiveOrZero
	Double						maxContractBudgetEUR;

	@PositiveOrZero
	Double						deviationContractBudgetEUR;

	@PositiveOrZero
	Double						avgContractBudgetUSD;

	@PositiveOrZero
	Double						minContractBudgetUSD;

	@PositiveOrZero
	Double						maxContractBudgetUSD;

	@PositiveOrZero
	Double						deviationContractBudgetUSD;

	@PositiveOrZero
	Double						avgContractBudgetGBP;

	@PositiveOrZero
	Double						minContractBudgetGBP;

	@PositiveOrZero
	Double						maxContractBudgetGBP;

	@PositiveOrZero
	Double						deviationContractBudgetGBP;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
