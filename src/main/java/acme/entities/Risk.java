
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}", message = "{validation.claim.code}")
	private String				reference;

	@NotNull
	@Past
	private Date				identificationDate;

	@NotNull
	@Positive
	private double				impact;

	@NotNull
	@Digits(integer = 1, fraction = 2)
	@Max(1)
	private double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	@Length(max = 255)
	private String				optionalLink;


	// Derived Attributes ---------------------------------------------------------
	public Double getValue() {
		return this.impact * this.probability;
	}

}
