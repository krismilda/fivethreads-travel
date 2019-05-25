package lt.fivethreads.services;

import lt.fivethreads.entities.request.OfficeDTO;
import lt.fivethreads.entities.rest.*;

import java.util.Date;
import java.util.List;

public interface StatisticService {
    List<TripCount> countTripList(Date start, Date finish, String role, String email);
    List<UserTripCountDTO> countTripByUser(Long [] IDList);
    //List<UserTripCountDTO> countTripByUser(IDList IDList);
    List<TripsByPrice> getTripsByPrice(String role, String email);
    List<TripByDuration> getTripByDuration(String role, String email);
    List<TripCountByOfficeDTO> getTripCountByOffice(String role, String email, Long [] offices);
}