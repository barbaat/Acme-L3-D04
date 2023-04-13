
package acme.features.authenticated.offer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.offers.Offer;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedOfferRepository extends AbstractRepository {

	@Query("select o from Offer o")
	List<Offer> findAllOffer();

	@Query("select o from Offer o where o.id = ?1")
	Offer findOfferById(Integer id);
}
