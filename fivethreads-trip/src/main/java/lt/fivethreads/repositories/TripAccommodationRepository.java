package lt.fivethreads.repositories;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.TripAccommodationForm;

import java.util.Date;
import java.util.List;

public interface TripAccommodationRepository {
    TripAccommodation findByID(long id);
    List<TripAccommodation> getAll();
    TripAccommodation saveTripAccommodation(TripAccommodation tripAccommodation);
    TripAccommodation updateTripAccommodation(TripAccommodation tripAccommodation);
    List<TripAccommodation> getAllByTrip(long tripId);
    List<TripAccommodation> getAllByUser(long userId);
    List<TripAccommodation> getAllByApartment(long apartmentId);
    List <Room> getUnoccupiedRooms(Date startDate, Date finishDate);
    List<Apartment> getUnoccupiedRoomApartments(Date startDate, Date finishDate);
    List<Office> getUnoccupiedRoomOffices(Date startDate, Date finishDate);
    List <Room> getUnoccupiedRoomsByApartment(Date startDate, Date finishDate, Long apartmentId);
    List <Apartment> getUnoccupiedApartmentsByOffice(Date startDate, Date finishDate, Long officeId);
}
