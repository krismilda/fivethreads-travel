package lt.fivethreads.repositories;

import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.User;
import lt.fivethreads.exception.WrongTripData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TripAccommodationRepositoryImplementation implements TripAccommodationRepository {

    @PersistenceContext
    EntityManager em;

    public TripAccommodation findByID(long id) { return em.find(TripAccommodation.class, id); }

    public List<TripAccommodation> getAll() { return em.createNamedQuery("TripAccommodation.FindAll",
            TripAccommodation.class).getResultList(); }

    public TripAccommodation createTripAccommodation(TripAccommodation tripAccommodation) {
        em.persist(tripAccommodation);
        return tripAccommodation;
    }

    public TripAccommodation updateTripAccommodation(TripAccommodation tripAccommodation) {
        return em.merge(tripAccommodation);
    }

    public List<TripAccommodation> getAllByTrip(long tripId) {
        return em.createNamedQuery("TripAccommodation.FindByTrip", TripAccommodation.class)
                .setParameter("trip_ID", tripId)
                .getResultList();
    }

    public List<TripAccommodation> getAllByUser(long userId) {
        return em.createNamedQuery("TripAccommodation.FindByUser", TripAccommodation.class)
                .setParameter("user_ID", userId)
                .getResultList();
    }

    public List<TripAccommodation> getAllByApartment(long apartmentId) {
        return em.createNamedQuery("TripAccommodation.FindByApartment", TripAccommodation.class)
                .setParameter("apartment_ID", apartmentId)
                .getResultList();
    }


}
