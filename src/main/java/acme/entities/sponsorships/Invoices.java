
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
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
	@Pattern(regexp = "IN-[0-9]{4}-[0-9]{4}", message = "{validation.invoices.code}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationTime; // Falta la restricción.

	@Positive
	private Integer				quantity;

	@PositiveOrZero
	private Double				tax;

	@URL
	@Length(max = 255)
	private String				optionalLink;

	// Derived attributes -----------------------------------------------------


	public Double getTotalAmount() {
		return this.quantity * this.tax;
	}

	// Relationships ----------------------------------------------------------


	@Valid
	@ManyToOne(optional = false)
	private Sponsorship sponsorship;
}
