package lt.fivethreads.mapper;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    @Autowired
    UserService userService;

    @Autowired
    TripMemberRepository tripMemberRepository;

    public Trip ConvertCreateTripFormToTrip(CreateTripForm form) {
        Trip trip = new Trip();
        trip.setStartDate(form.getStartDate());
        trip.setFinishDate(form.getFinishDate());
        trip.setArrival(form.getArrival());
        trip.setDeparture(form.getDeparture());
        for (TripMemberDAO tripMemberCreateDAO : form.getTripMembers()
        ) {
            TripMember tripMember =convertTripMemberDAOtoTripMember(tripMemberCreateDAO);
            tripMember.setTrip(trip);
            tripMember.setTripAcceptance(TripAcceptance.PENDING);
            trip.getTripMembers().add(tripMember);
        }
        return trip;
    }

    public TripMember convertTripMemberDAOtoTripMember(TripMemberDAO tripMemberDAO) {
        User user = userService.getUserByEmail(tripMemberDAO.getEmail());
        TripMember tripMember = new TripMember();
        tripMember.setUser(user);
        tripMember.setIsAccommodationNeeded(tripMemberDAO.getIsAccommodationNeeded());
        if (tripMemberDAO.getIsAccommodationNeeded() && tripMemberDAO.getAccommodationDAO() != null) {
            TripAccommodation tripAccommodation = new TripAccommodation();
            AccommodationDAO accommodationDAO = tripMemberDAO.getAccommodationDAO();
            tripAccommodation.setAccommodationStart(accommodationDAO.getAccommodationStart());
            tripAccommodation.setAccommodationFinish(accommodationDAO.getAccommodationFinish());
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        tripMember.setIsFlightTickedNeeded(tripMemberDAO.getIsFlightTickedNeeded());
        if (tripMemberDAO.getIsCarNeeded() && tripMemberDAO.getCarTicketDAO() != null) {
            CarTicket carTicket = new CarTicket();
            CarTicketDAO carTicketDAO = tripMemberDAO.getCarTicketDAO();
            carTicket.setCarRentStart(carTicketDAO.getCarRentStart());
            carTicket.setCarRentFinish(carTicketDAO.getCarRentFinish());
            carTicket.setTripMember(tripMember);
            tripMember.setCarTicket(carTicket);
        }
        tripMember.setIsCarNeeded(tripMemberDAO.getIsCarNeeded());
        return tripMember;
    }

    public TripCancellation convertCancelledTripToObject(CancelledTrip cancelledTrip){
        TripCancellation tripCancellation = new TripCancellation();
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(cancelledTrip.getTripID(),
                cancelledTrip.getEmail());
        tripCancellation.setTripMember(tripMember);
        tripCancellation.setReason(cancelledTrip.getReason());
        return tripCancellation;
    }
}
