package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;

public interface ITripStatusService {
    Boolean checkIfPlanned(Trip trip);
    Boolean checkIfCompleted(Trip trip);
    Boolean checkIfStarted(Trip trip);
}
