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

@Repository
@Transactional
public class TripAccommodationRepositoryImpl implements TripAccommodationRepository {

    @PersistenceContext
    EntityManager em;


    public void saveTripAccommodation(TripAccommodation tripAccommodation) {
        em.persist(tripAccommodation);
    }

    public void updateTripAccommodation(TripAccommodation tripAccommodation) {
        em.merge(tripAccommodation);
    }


}
