
package acme.testing.any.peep;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.peeps.Peep;
import acme.framework.repositories.AbstractRepository;

public interface AnyPeepTestRepository extends AbstractRepository {

	@Query("select p from Peep p")
	Collection<Peep> findAllPeeps();

}
