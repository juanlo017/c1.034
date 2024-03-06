
package acme.entities.codeAudits;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.auditRecords.AuditRecord;
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

	// Relationships -----------------------------------

	@OneToMany
	private List<AuditRecord>	auditRecords;

	/*
	 * //Derived atributes
	 * public void computeModeMark() { ¿En el service?
	 * if (this.auditRecords == null || this.auditRecords.isEmpty())
	 * this.mark = null; // No audit records, mark cannot be computed
	 * 
	 * // Count occurrences of each mark
	 * Map<String, Integer> markCount = new HashMap<>();
	 * for (AuditRecord ar : this.auditRecords) {
	 * String m = ar.getMark().toString();
	 * markCount.put(m, markCount.getOrDefault(m, 0) + 1);
	 * }
	 * 
	 * // Find the mark with the highest count (mode)
	 * String modeMark = null;
	 * int maxCount = 0;
	 * for (Map.Entry<String, Integer> entry : markCount.entrySet())
	 * if (entry.getValue() > maxCount) {
	 * modeMark = entry.getKey();
	 * maxCount = entry.getValue();
	 * }
	 * 
	 * this.mark = modeMark;
	 * }
	 */
}
