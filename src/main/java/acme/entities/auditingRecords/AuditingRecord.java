
package acme.entities.auditingRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.audits.Audit;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditingRecord extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	//a subject (not blank, shorter than 76 characters)
	@NotBlank
	@Length(max = 75)
	protected String			subject;

	//an assessment (not blank, shorter than 101 characters)
	@NotBlank
	@Length(max = 100)
	protected String			assessment;

	//the period during which the subject was audited (in the past, at least one hour long)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startTime;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				finishTime;

	//a mark (“A+”, “A”, “B”, “C”, “F”, or “F-“)
	@NotNull
	@Enumerated(EnumType.STRING)
	protected TypeMark			mark;

	//an optional link with further information.
	@URL
	protected String			moreInfo;

	protected boolean			correctionRecord;

	// Relationships ----------------------------------------------------------
	@Valid
	@ManyToOne(optional = false)
	@NotNull
	protected Audit				audit;

}
