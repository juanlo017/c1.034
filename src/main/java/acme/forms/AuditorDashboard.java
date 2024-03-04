
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import acme.entities.codeAudits.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	Map<Type, Integer>			auditsPerType;

	double						averageRecords;

	double						recordsDeviation;

	int							minRecords;

	int							maxRecords;

	double						averagePeriod;

	double						periodDeviation;

	int							minPeriod;

	int							maxPeriod;
}
