
package acme.entities.contracts;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id"), @Index(columnList = "recordId")
})
public class ProgressLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^PG-[A-Z]{1,2}-[0-9]{4}$")
	private String				recordId;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				registrationMoment;

	@NotBlank
	@Length(max = 75)
	private String				responsiblePerson;

	@PositiveOrZero
	@Max(value = 1)
	private Double				completeness;

	@NotBlank
	@Length(max = 100)
	private String				comment;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Valid
	@ManyToOne(optional = false)
	private Contract			contract;

}
