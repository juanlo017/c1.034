
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
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
import acme.client.data.accounts.Administrator;
import acme.entities.projects.Project;
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
	@Pattern(regexp = "^R-[0-9]{3}$", message = "{validation.risk.code}")
	private String				reference;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				identificationDate;

	@Positive
	@NotNull
	private Double				impact;

	@NotNull
	@Digits(integer = 1, fraction = 2)
	@Max(1)
	private Double				probability;

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


	//Relationships -----------------------------------------------------------
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Project			project;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Administrator	administrator;

}
