package lt.fivethreads.validation;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.FullAddressDTO;
import lt.fivethreads.exception.CannotCombineTrips;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TripAccommodationValidation {

    public void checkFinishStartDates(Date startDate, Date finishDate, String message) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if (formatter.parse(formatter.format(finishDate)).compareTo(formatter.parse(formatter.format(startDate))) < 0) {
                throw new WrongTripData(message);
            }
        }
        catch (ParseException e){
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }
    public void checkStartDateToday(Date startDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        try{
            if (formatter.parse(formatter.format(startDate)).compareTo(formatter.parse(formatter.format(today))) < 0) {
                throw new WrongTripData("Start date is earlier than today.");
            }
        }
        catch (ParseException e){
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }

    public void checkTripAccommodationDatesAgainstTripDates(Date startDate, Date finishDate, Trip trip){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if(formatter.parse(formatter.format(startDate)).compareTo(formatter.parse(formatter.format(trip.getStartDate()))) < 0)
                throw new WrongTripData("Start Date is earlier than trip start date.");
            if (formatter.parse(formatter.format(startDate)).compareTo(formatter.parse(formatter.format(trip.getFinishDate())))  > 0)
                throw new WrongTripData("Start Date precedes trip start date.");
            if (formatter.parse(formatter.format(finishDate)).compareTo(formatter.parse(formatter.format(trip.getFinishDate()))) > 0)
                throw new WrongTripData("Finish Date precedes trip finish date.");
            if (formatter.parse(formatter.format(finishDate)).compareTo(formatter.parse(formatter.format(trip.getStartDate()))) < 0)
                throw new WrongTripData("Finish Date is earlier trip start date.");
        }
        catch (ParseException e){
            throw new CannotCombineTrips("Wrong trip dates.");
        }
    }

    public void hotelRequiredFields(String hotelName, FullAddressDTO hotelAddress, Double price){
        if(hotelName == null)
            throw new WrongTripData("Hotel Name cannot be null.");
        if(hotelAddress == null)
            throw new  WrongTripData("Hotel Address cannot be null.");
        if(price == null)
            throw  new  WrongTripData("Price cannot be null.");
        if(price <=0)
            throw new WrongTripData("Price cannot be negative");
    }


    public void isAccommodationAccepted(TripMember tripMember){
        if(!(tripMember.getTripAcceptance() == TripAcceptance.ACCEPTED && tripMember.getIsAccommodationNeeded()))
            throw new WrongTripData(tripMember.getUser().getEmail()+" has not accepted accommodation");
    }

}
