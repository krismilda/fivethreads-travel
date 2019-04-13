package lt.fivethreads.validation;

import lt.fivethreads.entities.TripMember;
import lt.fivethreads.exception.WrongTripData;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TripValidation {

    public void checkFnishStartDates(Date startDate, Date finishDate, String message) {
        if (finishDate.compareTo(startDate) < 0) {
            throw new WrongTripData(message);
        }
    }


    public void checkStartDateToday(Date startDate) {
        Date today = new Date();
        if (startDate.compareTo(today) < 0) {
            throw new WrongTripData("Start date is earlier than today.");
        }
    }

    public void validateTripMember(TripMember tripMember) {
        if (tripMember.getIsAccommodationNeeded() && tripMember.getTripAccommodation() != null) {
            checkFnishStartDates(tripMember.getTripAccommodation().getAccommodationStart(),
                    tripMember.getTripAccommodation().getAccommodationFinish(),
                    "Finish date is earlier than start date.");
            checkFnishStartDates(tripMember.getTrip().getStartDate(),
                    tripMember.getTripAccommodation().getAccommodationStart(),
                    "Accommodation start date is earlier than trip start date.");
            checkFnishStartDates(tripMember.getTripAccommodation().getAccommodationFinish(),
                    tripMember.getTrip().getFinishDate(),
                    "Accommodation finish date is later than trip finish date.");
        }
        if (tripMember.getIsCarNeeded() && tripMember.getCarTicket() != null) {
            checkFnishStartDates(tripMember.getCarTicket().getCarRentStart(),
                    tripMember.getCarTicket().getCarRentFinish(),
                    "Finish date is earlier than start date.");
            checkFnishStartDates(tripMember.getTrip().getStartDate(),
                    tripMember.getCarTicket().getCarRentStart(),
                    "Car rent start date is earlier than trip start date.");
            checkFnishStartDates(tripMember.getCarTicket().getCarRentFinish(),
                    tripMember.getTrip().getFinishDate(),
                    "Car rent finish date is later than trip finish date.");
        }
        if(tripMember.getIsAccommodationNeeded() && tripMember.getTripAccommodation()==null){
            throw new WrongTripData("Accommodation data cannot be empty.");
        }
        if(tripMember.getIsCarNeeded() && tripMember.getCarTicket()==null){
            throw new WrongTripData("Car rent data cannot be empty.");
        }
    }
}
