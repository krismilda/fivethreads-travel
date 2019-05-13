package lt.fivethreads.services;

import lt.fivethreads.entities.Role;
import lt.fivethreads.entities.request.TripDTO;
import lt.fivethreads.entities.rest.DateRangeDTO;
import lt.fivethreads.entities.rest.TripCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class StatisticServiceImplementation implements StatisticService{

    @Autowired
    TripService tripService;

    public List<TripCount> countTripList(DateRangeDTO dateRangeDTO, String role, String email){
        List<TripDTO> tripList;
        if(role.equals("ROLE_ADMIN") || role.equals("ROLE_ORGANIZER") ){
            tripList=tripService.getAllTrips();
        }
        else{
            tripList=tripService.getAllTripsByUserEmail(email);
        }
        List<Date> allDates = this.getDatesListBetweenDates(dateRangeDTO.getStart(), dateRangeDTO.getFinish());
        List<TripCount> tripCountList = new ArrayList<>();
        for (Date day: allDates
             ) {
            int result = this.countTripInDay(tripList, day);
            TripCount tripCount = new TripCount();
            tripCount.setCount(result);
            tripCount.setDate(day);
            tripCountList.add(tripCount);
        }
        return tripCountList;
    }

    public int countTripInDay(List<TripDTO> trips, Date day){
        int count = 0;
        for (TripDTO trip: trips
        ) {
            if((trip.getStartDate().compareTo(day)<=0 && trip.getFinishDate().compareTo(day)>=0)){
                count++;
            }
        }
        return count;
    }

    public List<Date> getDatesListBetweenDates(Date start, Date finish){
        List<Date> dates = new ArrayList<>();
        dates.add(new Date(start.getTime()));
        while(start.compareTo(finish)<0)
        {
            start = new Date(start.getTime() + 86400000);
            dates.add(new Date(start.getTime()));
        }
        return dates;
    }
}
