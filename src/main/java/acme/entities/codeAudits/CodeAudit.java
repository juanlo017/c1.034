
package acme.entities.codeAudits;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.auditRecords.Mark;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CodeAudit extends AbstractEntity {

	// Serialisation identifier --------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -----------------------------------------------------
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}-[0-9]{3}$", message = "{validation.codeaudit.code}")
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				executionDate;

	private Type				type;

	@NotBlank
	@Length(max = 100)
	private String				actions;

	@Transient
	private String				mark;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				draftMode;

	// Relationships -----------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Auditor				auditor;


	//Derived atributes
	public Mark getMark(final Collection<AuditRecord> auditRecords) {

		Mark mark;
		if (auditRecords == null || auditRecords.isEmpty())
			mark = null; // No audit records, mark cannot be computed

		// Count occurrences of each mark
		Map<Mark, Integer> markCount = new HashMap<>();
		for (AuditRecord ar : auditRecords) {
			Mark m = ar.getMark();
			markCount.put(m, markCount.getOrDefault(m, 0) + 1);
		}

		// Find the mark with the highest count (mode)
		Mark modeMark = null;
		int maxCount = 0;
		for (Map.Entry<Mark, Integer> entry : markCount.entrySet())
			if (entry.getValue() > maxCount) {
				modeMark = entry.getKey();
				maxCount = entry.getValue();
			}

		mark = modeMark;
		return mark;
	}

}
