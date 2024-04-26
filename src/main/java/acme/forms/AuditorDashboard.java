
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	Integer						totalNumberOfStaticCodeAudit;
	Integer						totalNumberOfDynamicCodeAudit;

	Double						averageNumberOfAuditRecords;
	Double						deviationNumberOfAuditRecords;
	Integer						minimumNumberOfAuditRecords;
	Integer						maximumNumberOfAuditRecords;

	Double						averageTimeOfPeriod;
	Double						deviationTimeOfPeriod;
	Double						minimumTimeOfPeriod;
	Double						maximumTimeOfPeriod;
}
