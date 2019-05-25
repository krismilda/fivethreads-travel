package lt.fivethreads.validation;

import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.exception.CannotCombineTrips;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TripValidation {

    @Autowired
    EventService eventService;

    public void checkFnishStartDates(Date startDate, Date finishDate, String message) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date finishDate_new = formatter.parse(formatter.format(finishDate));
            Date startDate_new = formatter.parse(formatter.format(startDate));
            if (finishDate_new.compareTo(startDate_new) < 0) {
                throw new WrongTripData(message);
            }
        }
        catch (ParseException e){
            System.out.println(e);
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }


    public void checkStartDateToday(Date startDate) {
        try{
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            Date date_today_without_time = formatter.parse(formatter.format(today));
            Date start_without_time = formatter.parse(formatter.format(startDate));
            if (start_without_time.compareTo(date_today_without_time) < 0) {
                throw new WrongTripData("Start date is earlier than today.");
            }
        }
        catch (ParseException e){
            System.out.println(e);
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }

    public void validateTripMember(TripMember tripMember) {
        List<EventDTO> events = eventService.getUserEvents(tripMember.getUser().getId());
        Boolean isBusy = events.stream().anyMatch(e->((e.getStartDate().compareTo(tripMember.getTrip().getStartDate())>=0 & e.getStartDate().compareTo(tripMember.getTrip().getFinishDate())<=0) |
                (e.getEndDate().compareTo(tripMember.getTrip().getStartDate())>=0 & e.getStartDate().compareTo(tripMember.getTrip().getFinishDate())<=0)));
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
