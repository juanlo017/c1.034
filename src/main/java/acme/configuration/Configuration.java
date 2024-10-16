
package acme.configuration;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Configuration extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}$")
	protected String			currency;

	@NotBlank
	@Pattern(regexp = "^([A-Z]{3}(?:,|,\s))*[A-Z]{3}$")
	public String				acceptedCurrencies;

}
