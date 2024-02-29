
package acme.entities.sponsorships;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@Column(unique = true)
	private String			code;

	@Past
	private Date			moment;

	@Positive
	private Integer			amount;

	private TypeSporsonship	type;

	private String			optionalEmail;

	@URL
	private String			optionalLink;


	// Derived attributes -----------------------------------------------------
	public Integer getDurationInMonths() {
		if (this.moment != null) {
			LocalDate startDate = this.moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = LocalDate.now();
			Period period = Period.between(startDate, endDate);
			int months = period.getMonths() + period.getYears() * 12;
			return Math.max(months, 1);
		}
		return null;
	}

	// Relationships ----------------------------------------------------------


	@Valid
	@ManyToOne(optional = true)
	private Project project;
}
