
package acme.entities.projects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingModule extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}", message = "{validation.project.code}")
	private String				code;

	@Past
	private Date				creationMoment;

	@NotBlank
	@Length(max = 101)
	private String				details;

	private DifficultyLevel		difficultyLevel;

	@Past
	private Date				updateMoment;

	@URL
	private String				optionalLink;

	private Double				totalTime;
}
