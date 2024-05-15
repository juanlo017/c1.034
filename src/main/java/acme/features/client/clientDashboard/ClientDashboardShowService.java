
package acme.features.client.clientDashboard;

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
		int avgContractBudget;
		int minContractBudget;
		int maxContractBudget;
		int deviationContractBudget;

		clientDashboard = new ClientDashboard();

		numberOfProgressLogs = this.repository.numberOfProgressLogs(clientId);
		numberOfCompletedProgressLogs = this.repository.numberOfCompletedProgressLogs(clientId);
		avgContractBudget = this.repository.avgContractBudget(clientId);
		minContractBudget = this.repository.minContractBudget(clientId);
		maxContractBudget = this.repository.maxContractBudget(clientId);
		deviationContractBudget = this.repository.deviationContractBudget(clientId);

		clientDashboard.setNumberOfProgressLogs(numberOfProgressLogs);
		clientDashboard.setProgressLogsCompletenessRate(this.calculatePercentil(numberOfCompletedProgressLogs, numberOfProgressLogs));
		clientDashboard.setAvgContractBudget(avgContractBudget);
		clientDashboard.setMinContractBudget(minContractBudget);
		clientDashboard.setMaxContractBudget(maxContractBudget);
		clientDashboard.setDeviationContractBudget(deviationContractBudget);

		super.getBuffer().addData(clientDashboard);
	}

	private Percentil calculatePercentil(final int value, final int total) {

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
	public void unbind(final ClientDashboard object) {
		Dataset dataset = null;
		dataset = super.unbind(object, "numberOfProgressLogs", "progressLogsCompletenessRate", "avgContractBudget", "minContractBudget", "maxContractBudget", "deviationContractBudget");
		super.getResponse().addData(dataset);
	}
}
