package lt.fivethreads.repositories;

import lt.fivethreads.entities.Trip;

import java.util.List;

public interface TripRepository {
    Trip findByID(long id);
    List<Trip> getAll();
    void createTrip(Trip trip);
    List<Trip> getAllByOrganizerEmail(String email);
    List<Trip> getAllByUserEmail(String email);
    void updateTrip(Trip trip);
    void deleteTrip(Trip trip);
    void combineTrips(Trip newTrip, Trip trip1, Trip trip2);
}
