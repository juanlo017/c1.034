
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				instantiationMoment;

	@NotBlank
	@URL
	@Length(max = 255)
	private String				pictureLink;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@URL
	@NotBlank
	@Length(max = 255)
	private String				link;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayPeriodStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				displayPeriodEnd;


	@AssertTrue(message = "El display period debe comenzar después del instantiation moment")
	private boolean isDisplayPeriodValid() {
		if (this.instantiationMoment == null || this.displayPeriodStart == null)
			return false;
		return MomentHelper.isAfter(this.displayPeriodStart, this.instantiationMoment);
	}

}
