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
    void deleteTripAccommodation(Long id);
    List<TripAccommodation> getAllReservations(Long roomID);
}
