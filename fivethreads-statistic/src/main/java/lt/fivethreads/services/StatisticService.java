package lt.fivethreads.services;

import lt.fivethreads.entities.Role;
import lt.fivethreads.entities.rest.DateRangeDTO;
import lt.fivethreads.entities.rest.TripCount;

import java.util.List;

public interface StatisticService {
    List<TripCount> countTripList(DateRangeDTO dateRangeDTO,  String role, String email);
}
