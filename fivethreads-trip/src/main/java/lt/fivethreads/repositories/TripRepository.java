package lt.fivethreads.repositories;

import lt.fivethreads.entities.Trip;

import java.util.List;

public interface TripRepository {
    Trip findByID(long id);
    List<Trip> getAll();
    void createTrip(Trip trip);
}
