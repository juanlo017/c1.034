
package acme.entities.contracts;

import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.entities.projects.Project;

public class Contract extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$")
	private String				code;

	@Past
	private Timestamp			instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				providerName;

	@NotBlank
	@Length(max = 75)
	private String				customerName;

	@NotBlank
	@Length(max = 100)
	private String				goals;

	@PositiveOrZero
	//@Max(value = project.getCost()) TODO implement this
	private Money				budget;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Valid
	@ManyToOne(optional = false)
	private Project				project;

}
