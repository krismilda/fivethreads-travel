package lt.fivethreads.validation;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.exception.WrongOccupancyData;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OccupancyValidation {

    public void checkFinishStartDates(Date startDate, Date finishDate, String message) {
        if (finishDate.compareTo(startDate) < 0) {
            throw new WrongOccupancyData(message);
        }
    }
    public void checkStartDateToday(Date startDate) {
        Date today = new Date();
        if (startDate.compareTo(today) < 0) {
            throw new WrongOccupancyData("Start date is earlier than today.");
        }
    }

    public void checkOccupancyDatesAgainstTripDates(Date startDate, Date finishDate, Trip trip, String message){
        if(startDate.compareTo(trip.getStartDate()) < 0)
            throw new WrongOccupancyData("Start Date is earlier than trip start date.");
        if (startDate.compareTo(trip.getFinishDate())  > 0)
            throw new WrongOccupancyData("Start Date precedes trip start date.");
        if (finishDate.compareTo(trip.getFinishDate()) > 0)
            throw new WrongOccupancyData("Finish Date precedes trip finish date.");
        if (finishDate.compareTo(trip.getStartDate()) < 0)
            throw new WrongOccupancyData("Finish Date is earlier trip start date.");
    }
}
