
package acme.entities.auditRecords;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

	// Serialisation identifier --------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes-------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}", message = "{validation.auditrecord.code}")
	private String				code;

	@NotNull
	@Past
	private Date				startTime;

	@NotNull
	@Past
	private Date				endTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Long				auditPeriod;

	private Mark				mark;

	@URL
	@Length(max = 255)
	private String				link;


	public void getPeriod() {
		if (this.startTime != null && this.endTime != null) {
			Long duration;
			duration = MomentHelper.computeDuration(this.startTime, this.endTime).getSeconds();
			if (duration >= 3600)
				this.auditPeriod = duration;
		}
		this.auditPeriod = null;
	}

}