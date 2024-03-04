
package acme.components;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;

import acme.client.data.datatypes.Money;
import acme.client.helpers.MessageHelper;
import acme.client.helpers.StringHelper;

public class MoneyFormatter implements Formatter<Money> {

	@Override
	public String print(final Money money, final Locale locale) {
		assert money != null;
		assert locale != null;

		return money.toString();
	}

	@Override
	public Money parse(final String text, final Locale locale) throws ParseException {
		assert !StringHelper.isBlank(text);
		assert locale != null;

		// Parse the text to extract the amount and currency
		String amountRegex = "\\d+(\\.\\d{1,2})?";
		String currencyRegex = "[A-Za-z]{3}"; // Currency is represented by 3 letters
		String moneyRegex = String.format("^(?<AMOUNT>%s)\\s+(?<CURRENCY>%s)$", amountRegex, currencyRegex);

		Pattern pattern = Pattern.compile(moneyRegex);
		Matcher matcher = pattern.matcher(text);

		if (!matcher.find()) {
			String errorMessage = MessageHelper.getMessage("default.error.conversion", null, "Invalid value", locale);
			throw new ParseException(0, errorMessage);
		}

		// Extract amount and currency from matcher groups
		String amountStr = matcher.group("AMOUNT");
		String currency = matcher.group("CURRENCY");

		Double amount = Double.parseDouble(amountStr);

		Money money = new Money();
		money.setAmount(amount);
		money.setCurrency(currency);

		return money;
	}
}
