package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripValidation tripValidation;

    public void checkNotification(Notification notification) {

    }

    public void sendNotificationsCreatedTrip(Trip trip) {

    }

    public void tripAccepted(AcceptedTrip acceptedTrip) {
        TripMember tripMember = tripMapper.convertTripMemberDAOtoTripMember(acceptedTrip.getTripMemberDAO());
        Trip trip = tripRepository.findByID(acceptedTrip.getTripID());
        if (trip == null) {
            throw new WrongTripData("Trip ID does not exist.");
        }
        tripMember.setTrip(trip);
        TripMember tripMemberOld = tripMember.getTrip()
                .getTripMembers()
                .stream()
                .filter(t -> t.getUser().getEmail().equals(acceptedTrip.getTripMemberDAO().getEmail()))
                .findFirst()
                .orElseThrow(() -> new WrongTripData("User's email is wrong."));
        tripMember.setId(tripMemberOld.getId());
        tripValidation.validateTripMember(tripMember);
        tripMember.setTripAcceptance(TripAcceptance.ACCEPTED);
        tripMemberRepository.updateTripMember(tripMember);
    }

    public void tripCancelled(CancelledTrip cancelledTrip) {
        TripCancellation tripCancellation = tripMapper.convertCancelledTripToObject(cancelledTrip);
        tripCancellation.getTripMember().setTripCancellation(tripCancellation);
        tripCancellation.getTripMember().setTripAcceptance(TripAcceptance.CANCELLED);
        tripMemberRepository.addCancellation(tripCancellation);
    }
}
