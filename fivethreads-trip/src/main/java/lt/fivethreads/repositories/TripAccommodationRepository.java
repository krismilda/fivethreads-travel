package lt.fivethreads.repositories;

import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripMember;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface TripAccommodationRepository {
    void saveTripAccommodation(TripAccommodation tripAccommodation);
    void updateTripAccommodation(TripAccommodation tripAccommodation);
}
