
package acme.features.sponsor.dashboard;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.configuration.Configuration;
import acme.entities.sponsorships.Invoices;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	@Autowired
	private SponsorDashboardRepository sdr;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		SponsorDashboard sponsorDashboard;

		sponsorDashboard = new SponsorDashboard();

		Collection<Invoices> myPublishedInvoices = this.sdr.findManyInvoicesBySponsorId(sponsorId).stream().filter(x -> !x.isDraftMode()).toList();

		Collection<Money> myQuantities = this.sdr.findManyPublishedQuantitiesBySponsorId(sponsorId);
		Collection<Money> myAmounts = this.sdr.findManyPublishedAmountsBySponsorId(sponsorId); //this only considers published contracts.

		Map<String, List<Money>> amountsByCurrency = myAmounts.stream().collect(Collectors.groupingBy(Money::getCurrency));
		Map<String, List<Money>> quantitiesByCurrency = myQuantities.stream().collect(Collectors.groupingBy(Money::getCurrency));

		Map<String, Double> avgAmountsPorCurrency = amountsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMedia(entry.getValue()).getAmount()));
		Map<String, Double> maxAmountsPorCurrency = amountsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMaximo(entry.getValue()).getAmount()));
		Map<String, Double> minAmountsPorCurrency = amountsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMinimo(entry.getValue()).getAmount()));
		Map<String, Double> desvAmountsPorCurrency = amountsByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularDesviacion(entry.getValue()).getAmount()));

		// Cálculos con quantities
		Map<String, Double> avgQuantitiesPorCurrency = quantitiesByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMedia(entry.getValue()).getAmount()));
		Map<String, Double> maxQuantitiesPorCurrency = quantitiesByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMaximo(entry.getValue()).getAmount()));
		Map<String, Double> minQuantitiesPorCurrency = quantitiesByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularMinimo(entry.getValue()).getAmount()));
		Map<String, Double> desvQuantitiesPorCurrency = quantitiesByCurrency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> this.calcularDesviacion(entry.getValue()).getAmount()));

		List<Configuration> configuration = this.sdr.findConfiguration();
		String[] supportedCurrencies = configuration.get(0).acceptedCurrencies.split(",");

		double totalNumInvoicesWithTaxLessOrEqualTo21 = myPublishedInvoices.stream().filter(x -> x.getTax() <= 21.00).count();
		double totalNumInvoicesWithLink = myPublishedInvoices.stream().filter(x -> !x.getOptionalLink().equals(null)).count();

		sponsorDashboard.setTotalNumInvoicesWithTaxLessOrEqualTo21(totalNumInvoicesWithTaxLessOrEqualTo21);
		sponsorDashboard.setTotalNumInvoicesWithLink(totalNumInvoicesWithLink);

		sponsorDashboard.setAverageSponsorshipsAmount(avgAmountsPorCurrency);
		sponsorDashboard.setMaximumSponsorshipsAmount(maxAmountsPorCurrency);
		sponsorDashboard.setMinimumSponsorshipsAmount(minAmountsPorCurrency);
		sponsorDashboard.setDeviationSponsorshipsAmount(desvAmountsPorCurrency);

		sponsorDashboard.setAverageInvoicesQuantity(avgQuantitiesPorCurrency);
		sponsorDashboard.setMaximumInvoicesQuantity(maxQuantitiesPorCurrency);
		sponsorDashboard.setMinimumInvoicesQuantity(minQuantitiesPorCurrency);
		sponsorDashboard.setDeviationInvoicesQuantity(desvQuantitiesPorCurrency);

		sponsorDashboard.setSupportedCurrencies(supportedCurrencies);
		super.getBuffer().addData(sponsorDashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {

		Dataset dataset;

		dataset = super.unbind(object, "totalNumInvoicesWithTaxLessOrEqualTo21", "totalNumInvoicesWithLink", "averageSponsorshipsAmount", "deviationSponsorshipsAmount", "minimumSponsorshipsAmount", "maximumSponsorshipsAmount", "averageInvoicesQuantity",
			"deviationInvoicesQuantity", "minimumInvoicesQuantity", "maximumInvoicesQuantity", "supportedCurrencies");

		super.getResponse().addData(dataset);

	}

	private Money calcularMedia(final Collection<Money> money) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		moneyFinal.setAmount(money.stream().map(x -> x.getAmount()).mapToDouble(Double::doubleValue).average().orElse(Double.NaN));

		return moneyFinal;
	}

	private Money calcularMaximo(final Collection<Money> money) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		moneyFinal.setAmount(money.stream().map(x -> x.getAmount()).mapToDouble(Double::doubleValue).max().orElse(Double.NaN));
		return moneyFinal;
	}

	private Money calcularMinimo(final Collection<Money> money) {
		Money moneyFinal = new Money();
		moneyFinal.setCurrency("USD");
		moneyFinal.setAmount(money.stream().map(x -> x.getAmount()).mapToDouble(Double::doubleValue).min().orElse(Double.NaN));
		return moneyFinal;

	}

	private Money calcularDesviacion(final Collection<Money> money) {
		Money desviacion = new Money();
		desviacion.setCurrency("USD");

		// Calculo de la media
		double media = money.stream().mapToDouble(Money::getAmount).average().orElse(Double.NaN);

		// Calculo de la suma de las diferencias al cuadrado
		double sumaDiferenciasCuadradas = money.stream().mapToDouble(budget -> Math.pow(budget.getAmount() - media, 2)).sum();

		// Calculo de la varianza
		double varianza = sumaDiferenciasCuadradas / money.size();

		// Calculo de la desviación estándar como la raíz cuadrada de la varianza
		double desviacionEstandar = Math.sqrt(varianza);

		desviacion.setAmount(desviacionEstandar);

		return desviacion;
	}

}
