
package acme.entities.projects;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "id"), @Index(columnList = "project_id"), @Index(columnList = "user_story_id"), @Index(columnList = "project_id,user_story_id")
})
public class Assignment extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private Project				project;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	private UserStory			userStory;

}
