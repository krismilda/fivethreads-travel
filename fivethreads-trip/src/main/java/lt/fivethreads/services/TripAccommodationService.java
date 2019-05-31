package lt.fivethreads.services;

import lt.fivethreads.entities.request.*;

import java.util.List;

public interface TripAccommodationService {
    TripAccommodationDTO getTripAccommodation(long tripAccommodationId);
    TripAccommodationDTO createTripAccommodation(TripAccommodationForm tripAccommodationForm);
    TripAccommodationDTO updateTripAccommodation(TripAccommodationDTO tripAccommodationDTO);
    List<TripAccommodationDTO> getAllTripAccommodations();
    List<TripAccommodationDTO> getAllTripAccommodationsByUser(long userId);
    List<TripAccommodationDTO> getAllTripAccommodationsByTrip(long tripId);
    List<TripAccommodationDTO> getAllTripAccommodationsByApartment(long apartmentId);
    void deleteTripAccommodation(Long id);
    List<RoomReservations> getAllReservations(Long roomID);
    TripAccommodationDTO getTripAccommodationsByTrioMember(long tripMemberID);
}
