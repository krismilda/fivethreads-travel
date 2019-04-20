package lt.fivethreads.repositories;

import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripMember;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

public interface TripAccommodationRepository {
    TripAccommodation findByID(long id);
    List<TripAccommodation> getAll();
    TripAccommodation createTripAccommodation(TripAccommodation tripAccommodation);
    TripAccommodation updateTripAccommodation(TripAccommodation tripAccommodation);
    List<TripAccommodation> getAllByTrip(long tripId);
    List<TripAccommodation> getAllByUser(long userId);
    List<TripAccommodation> getAllByApartment(long apartmentId);
    List <Room> getUnoccupiedRooms(Date startDate, Date finishDate);
}
