package lt.fivethreads.services;

import lt.fivethreads.entities.request.CombineTwoTrips;
import lt.fivethreads.entities.request.TripDTO;

import java.util.List;

public interface CombineTripsService {
    TripDTO combineTwoTrips(CombineTwoTrips combineTwoTrips, String organizer_email);
    List<TripDTO> getListForCombination(Long tripID);
    }
