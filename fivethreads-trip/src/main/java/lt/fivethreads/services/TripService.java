package lt.fivethreads.services;

import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.entities.request.TripDTO;

import java.util.List;

public interface TripService {
    void createTrip(CreateTripForm form);
    List<TripDTO> getAllTrips();
    List<TripDTO> getAllTripsByOrganizerEmail(String email);
    List<TripDTO> getAllTripsByUserEmail(String email);
}
