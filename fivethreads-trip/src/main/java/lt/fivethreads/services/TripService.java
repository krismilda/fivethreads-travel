package lt.fivethreads.services;

import lt.fivethreads.entities.request.CreateTripForm;

public interface TripService {
    void createTrip(CreateTripForm form);
}
