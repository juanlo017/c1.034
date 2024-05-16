
package acme.features.client.clientDashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.forms.Percentil;
import acme.roles.Client;

@Service
public class ClientDashboardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected ClientDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Principal principal;
		int clientId;

		principal = super.getRequest().getPrincipal();
		clientId = principal.getActiveRoleId();

		ClientDashboard clientDashboard;

		int numberOfProgressLogs;
		int numberOfCompletedProgressLogs;

		Double avgContractBudgetEUR;
		Double minContractBudgetEUR;
		Double maxContractBudgetEUR;
		Double deviationContractBudgetEUR;

		Double avgContractBudgetUSD;
		Double minContractBudgetUSD;
		Double maxContractBudgetUSD;
		Double deviationContractBudgetUSD;

		Double avgContractBudgetGBP;
		Double minContractBudgetGBP;
		Double maxContractBudgetGBP;
		Double deviationContractBudgetGBP;

		clientDashboard = new ClientDashboard();

		numberOfProgressLogs = this.repository.numberOfProgressLogs(clientId);
		numberOfCompletedProgressLogs = this.repository.numberOfCompletedProgressLogs(clientId);

		Map<String, Map<String, Double>> statsForEachCurrency = this.calculateStatsForEachCurrency(clientId);

		/*
		 * avgContractBudget = statsForEachCurrency.get("EUR").get("avg");
		 * minContractBudget = statsForEachCurrency.get("EUR").get("min");
		 * maxContractBudget = statsForEachCurrency.get("EUR").get("max");
		 * deviationContractBudget = statsForEachCurrency.get("EUR").get("dev");
		 * 
		 * clientDashboard.setNumberOfProgressLogs(numberOfProgressLogs);
		 * clientDashboard.setProgressLogsCompletenessRate(this.calculatePercentil(numberOfCompletedProgressLogs, numberOfProgressLogs));
		 * clientDashboard.setAvgContractBudget(avgContractBudget);
		 * clientDashboard.setMinContractBudget(minContractBudget);
		 * clientDashboard.setMaxContractBudget(maxContractBudget);
		 * clientDashboard.setDeviationContractBudget(deviationContractBudget);
		 */

		super.getBuffer().addData(clientDashboard);
	}

	private Map<String, Map<String, Double>> calculateStatsForEachCurrency(final int clientId) {

		Map<String, Map<String, Double>> res = new HashMap<>();

		String acceptedCurrencies = this.repository.findAcceptedCurrencies();
		List<String> currencies = List.of(acceptedCurrencies.split(","));

		for (String currency : currencies) {

			Double avgContractBudget;
			Double minContractBudget;
			Double maxContractBudget;
			Double deviationContractBudget;

			Map<String, Double> aux = new HashMap<>();

			avgContractBudget = this.repository.avgContractBudgetForCurrency(clientId, currency);
			minContractBudget = this.repository.minContractBudgetForCurrency(clientId, currency);
			maxContractBudget = this.repository.maxContractBudgetForCurrency(clientId, currency);
			deviationContractBudget = this.repository.deviationContractBudgetForCurrency(clientId, currency);

			Function<Object, Double> nullIsZeroFunction = x -> x == null ? .0 : (Double) x;

			aux.put("avg", nullIsZeroFunction.apply(avgContractBudget));
			aux.put("min", nullIsZeroFunction.apply(minContractBudget));
			aux.put("max", nullIsZeroFunction.apply(maxContractBudget));
			aux.put("dev", nullIsZeroFunction.apply(deviationContractBudget));

			res.put(currency, aux);
		}

		return res;
	}

	private Percentil calculatePercentil(final int value, int total) {

		if (total == 0)
			total = 1;

		double res = value / total;
		if (res < 0.25)
			return Percentil.BELOW_25_PERCENT;
		else if (0.25 < res && res < 0.5)
			return Percentil.BETWEEN_25_AND_50_PERCENT;
		else if (0.5 < res && res < 0.75)
			return Percentil.BETWEEN_50_AND_75_PERCENT;
		else
			return Percentil.ABOVE_75_PERCENT;
	}

	@Override
	public void unbind(final ClientDashboard clientDashboard) {
		Dataset dataset = null;
		dataset = super.unbind(clientDashboard, "numberOfProgressLogs", "progressLogsCompletenessRate", "avgContractBudget", "minContractBudget", "maxContractBudget", "deviationContractBudget");

		super.getResponse().addData(dataset);
	}
}
