package lt.fivethreads.services;

import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.rest.*;

import java.util.List;

public interface StatisticService {
    List<TripCount> countTripList(DateRangeDTO dateRangeDTO,  String role, String email);
    List<UserTripCountDTO> countTripByUser(IDList IDList);
    //List<UserTripCountDTO> countTripByUser(IDList IDList);
    List<TripsByPrice> getTripsByPrice(String role, String email);
    List<TripByDuration> getTripByDuration(String role, String email);
    List<TripCountByOfficeDTO> getTripCountByOffice(String role, String email);
}