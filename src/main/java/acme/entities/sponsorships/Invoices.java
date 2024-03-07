
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Invoices extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^IN-[0-9]{4}-[0-9]{4}$", message = "{validation.invoices.code}")
	private String				code;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationTime;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dueDate; // Falta restricción de que es al menos un mes antes del registrationTime

	//	@Positive
	@NotNull
	private Money				quantity;

	@Digits(integer = 1, fraction = 2)
	@Max(1)
	@NotNull
	@PositiveOrZero
	private Double				tax;

	@URL
	@Length(max = 255)
	private String				optionalLink;


	// Derived attributes -----------------------------------------------------
	@Transient
	public Double getTotalAmount() {
		return this.quantity.getAmount() * this.tax + this.quantity.getAmount();
	}


	// Relationships ----------------------------------------------------------
	@Valid
	@ManyToOne(optional = false)
	private Sponsorship sponsorship;
}
