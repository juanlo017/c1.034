
package acme.roles;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;

public class Auditor extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Length(max = 76)
	private String				firm;

	@NotBlank
	@Length(max = 26)
	private String				professionalId;

	@NotBlank
	@Length(max = 101)
	private String				certifications;

	@URL
	@Length(max = 255)
	private String				link;

}