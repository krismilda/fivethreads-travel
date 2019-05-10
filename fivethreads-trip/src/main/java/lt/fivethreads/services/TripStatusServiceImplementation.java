package lt.fivethreads.services;

import lt.fivethreads.entities.AccommodationType;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripAcceptance;
import lt.fivethreads.entities.TripMember;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TripStatusServiceImplementation implements ITripStatusService
{
    public Boolean checkIfPlanned(Trip trip){
        Boolean status = true;
        for (TripMember tripMember: trip.getTripMembers()
             ) {
            if(!(!(tripMember.getTripAcceptance().equals(TripAcceptance.CANCELLED)) && tripMember.getIsFlightTickedNeeded() && tripMember.getFlightTicket()!=null && tripMember.getFlightTicket().getFile()!=null)){
                status=false;
            }
            if(!(tripMember.getIsCarNeeded() && tripMember.getCarTicket()!=null && tripMember.getCarTicket().getFile()!=null)){
                status=false;
            }
            if(!(tripMember.getIsAccommodationNeeded() && tripMember.getTripAccommodation()!=null  && tripMember.getTripAccommodation().getFile()!=null)){
                status=false;
            }
        }
        return status;
    }
    public Boolean checkIfCompleted(Trip trip){
        Date today = new Date();
        if(trip.getStartDate().compareTo(today)>=0 && trip.getFinishDate().compareTo(today)>0){
            return true;
        }
        return false;
    }

    public Boolean checkIfStarted(Trip trip){
        Date today = new Date();
        if(trip.getStartDate().compareTo(today)>=0 && trip.getFinishDate().compareTo(today)>=0){
            return true;
        }
        return false;
    }
}
