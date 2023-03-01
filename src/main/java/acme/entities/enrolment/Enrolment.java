
package acme.entities.enrolment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {
	//Una inscripción es un registro de un estudiante en un curso. El sistema debe almacenar los siguientes datos sobre ellos: 
	//un código (patrón “[A-Z]{1,3}[0-9][0-9]{3}”, no en blanco, único), una motivación (no en blanco, menos de 76 caracteres), 
	//unos objetivos (no en blanco, menos de 101 caracteres) y un tiempo de trabajo (en horas, computado a partir de las actividades
	//correspondientes).

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}[0-9][0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 76)
	protected String			motivation;

	@NotBlank
	@Length(max = 101)
	protected String			goals;

	//Me falta el tiempo de trabajo

}