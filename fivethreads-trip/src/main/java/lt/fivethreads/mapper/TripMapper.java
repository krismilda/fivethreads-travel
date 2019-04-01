package lt.fivethreads.mapper;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripAcceptance;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.CreateTripForm;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    @Autowired
    UserService userService;

    public Trip ConvertCreateTripFormToTrip(CreateTripForm form){
        Trip trip = new Trip();
        trip.setStartDate(form.getStartDate());
        trip.setFinishDate(form.getFinishDate());
        trip.setArrival(form.getArrival());
        trip.setDeparture(form.getDeparture());
        for (Long id:form.getTripMembersID()
             ) {
            User user = userService.getUserByID(id);
            TripMember tripMember = new TripMember();
            tripMember.setUser(user);
            tripMember.setTrip(trip);
            tripMember.setTripAcceptance(TripAcceptance.PENDING);
            trip.getTripMembers().add(tripMember);
        }
        return trip;
    }
}
