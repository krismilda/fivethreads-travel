package lt.fivethreads.services;

import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.TripAccommodationForm;

import java.util.List;

public interface TripAccommodationService {
    TripAccommodationDTO getTripAccommodation(long tripAccommodationId);
    TripAccommodationDTO createTripAccommodation(TripAccommodationForm tripAccommodationForm);
    List<TripAccommodationDTO> getAllTripAccommodations();
    List<TripAccommodationDTO> getAllTripAccommodationsByUser(long userId);
    List<TripAccommodationDTO> getAllTripAccommodationsByTrip(long tripId);
    List<TripAccommodationDTO> getAllTripAccommodationsByApartment(long apartmentId);

}
