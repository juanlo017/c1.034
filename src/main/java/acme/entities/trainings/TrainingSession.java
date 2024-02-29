
package acme.entities.trainings;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingSession extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "TS-[A-Z]{1,3}-[0-9]{3}", message = "{validation.project.code}")
	private String				code;

	@NotNull
	private LocalDateTime		timePeriodStart;

	@NotNull
	private LocalDateTime		timePeriodEnd;

	@NotBlank
	@Length(max = 76)
	private String				location;

	@NotBlank
	@Length(max = 76)
	private String				instructor;

	@NotBlank
	@Email
	private String				email;

	@URL
	@Length(max = 255)
	private String				link;

	// Relationships ----------------------------------------------------------

	@Valid
	@ManyToOne(optional = false)
	private TrainingModule		trainingModule;

	// Custom validators--------------------------------------------------------


	public void setTimePeriod(final LocalDateTime start, final LocalDateTime end) {
		this.timePeriodStart = start;
		this.timePeriodEnd = end;
	}

	// Getter for the timePeriod attribute
	public LocalDateTime getTimePeriodStart() {
		return this.timePeriodStart;
	}

	// Custom validation for timePeriod
	public boolean isValidTimePeriod() {
		if (this.timePeriodStart == null || this.timePeriodEnd == null)
			return false;

		// Check if start is at least one week ahead of creation moment
		LocalDateTime creationMoment = this.trainingModule.getCreationMoment();
		LocalDateTime oneWeekAhead = creationMoment.plusWeeks(1);
		if (this.timePeriodStart.isBefore(oneWeekAhead))
			return false;

		// Check if duration is at least one week long
		if (this.timePeriodStart.plusWeeks(1).isAfter(this.timePeriodEnd))
			return false;

		return true;
	}

}
