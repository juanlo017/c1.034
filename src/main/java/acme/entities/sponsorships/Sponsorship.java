
package acme.entities.sponsorships;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.client.helpers.MomentHelper;
import acme.entities.projects.Project;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}", message = "{validation.sponsorship.code}")
	@Column(unique = true)
	private String				code;

	@Past
	private Date				moment;

	@Positive
	private Money				amount;

	private TypeSponsorship		type;

	@Email
	private String				optionalEmail;

	@URL
	@Length(max = 255)
	private String				optionalLink;


	// Derived attributes -----------------------------------------------------
	@Transient
	public Integer getDuration() {
		if (this.moment != null) {
			Date currentMoment = MomentHelper.getCurrentMoment();
			Duration duration = MomentHelper.computeDuration(this.moment, currentMoment);
			long months = duration.toDays() / 30;
			return Math.toIntExact(months);
		}
		return null;
	}

	@Transient
	public boolean isDurationAtLeastOneMonthLong() {
		return this.getDuration() != null && this.getDuration() >= 1;
	}

	// Relationships ----------------------------------------------------------


	@Valid
	@ManyToOne(optional = true)
	private Project project;
}
