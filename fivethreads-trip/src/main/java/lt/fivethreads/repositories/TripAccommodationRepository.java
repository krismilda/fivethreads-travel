package lt.fivethreads.repositories;

import lt.fivethreads.entities.*;

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
    List <Room> getUnoccupiedRooms(Date startDate, Date finishDate);//copied
    List<Apartment> getUnoccupiedRoomApartments(Date startDate, Date finishDate);//copied
    List<Office> getUnoccupiedRoomOffices(Date startDate, Date finishDate);//copied
    List <Room> getUnoccupiedRoomsByApartment(Date startDate, Date finishDate, Long apartmentId);//copied
    List <Apartment> getUnoccupiedApartmentsByOffice(Date startDate, Date finishDate, Long officeId);//copied
}
