package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.NotificationDTO;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.mapper.NotificationMapper;
import lt.fivethreads.mapper.TripMapper;
import lt.fivethreads.mapper.TripMemberMapper;
import lt.fivethreads.repositories.NotificationRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import lt.fivethreads.validation.TripValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    TripMemberMapper tripMemberMapper;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationMapper notificationMapper;

    public void createNotifications(Trip trip, String name) {
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            this.createNotificationForTripMember(tripMember, name);
        }

    }

    public void createNotificationForTripMember(TripMember tripMember, String name){
        Notification notification = new Notification();
        notification.setName(name);
        notification.setIsActive(Boolean.TRUE);
        notification.setCreated_date(new Date());
        TripHistory tripHistory = new TripHistory();
        tripHistory.setStartDate(tripMember.getTrip().getStartDate());
        tripHistory.setFinishDate(tripMember.getTrip().getFinishDate());
        tripHistory.setArrival(tripMember.getTrip().getArrival());
        tripHistory.setDeparture(tripMember.getTrip().getDeparture());
        tripHistory.setOrganizer(tripMember.getTrip().getOrganizer());
        tripHistory.setIsFlightTickedNeeded(tripMember.getIsFlightTickedNeeded());
        tripHistory.setIsCarNeeded(tripMember.getIsCarNeeded());
        tripHistory.setIsAccommodationNeeded(tripMember.getIsAccommodationNeeded());
        if (tripHistory.getIsAccommodationNeeded()) {
            tripHistory.setAccommodationPrice(tripMember.getTripAccommodation().getPrice());
            tripHistory.setAccommodationStart(tripMember.getTripAccommodation().getAccommodationStart());
            tripHistory.setAccommodationFinish(tripMember.getTripAccommodation().getAccommodationFinish());
        }
        if (tripHistory.getIsCarNeeded()) {
            tripHistory.setCarRentStart(tripMember.getCarTicket().getCarRentStart());
            tripHistory.setCarRentFinish(tripMember.getCarTicket().getCarRentFinish());
            if(tripMember.getCarTicket().getPrice()!=null){
                tripHistory.setCarPrice(tripMember.getCarTicket().getPrice());
            }
        }
        if (tripMember.getFlightTicket() != null) {
            tripHistory.setFlightPrice(tripMember.getFlightTicket().getPrice());
        }
        List<TripMemberHistory> tripMemberHistoryList = new ArrayList<>();
        for (TripMember tripOtherMember : tripMember.getTrip().getTripMembers()
        ) {
            if (tripMember.getUser().getId() != tripOtherMember.getUser().getId()) {
                TripMemberHistory tripMemberHistory = new TripMemberHistory();
                tripMemberHistory.setEmail(tripOtherMember.getUser().getEmail());
                tripMemberHistory.setId(tripOtherMember.getUser().getId());
                tripMemberHistory.setFirstname(tripOtherMember.getUser().getFirstname());
                tripMemberHistory.setLastName(tripOtherMember.getUser().getLastName());
                tripMemberHistory.setPhone(tripOtherMember.getUser().getPhone());
                tripMemberHistory.setTripHistory(tripHistory);
                tripMemberHistoryList.add(tripMemberHistory);
            }
        }
        notification.setUser(tripMember.getUser());
        notification.setTrip(tripMember.getTrip());
        notification.setTripHistory(tripHistory);
        notification.getTripHistory().setTripMembers(tripMemberHistoryList);
        notificationRepository.saveNotification(notification);
    }

    public void tripAccepted(AcceptedTrip acceptedTrip) {
        TripMember tripMember = tripMemberMapper.convertTripMemberDAOtoTripMember(acceptedTrip.getTripMemberDTO());
        Trip trip = tripRepository.findByID(acceptedTrip.getTripID());
        if (trip == null) {
            throw new WrongTripData("Trip ID does not exist.");
        }
        tripMember.setTrip(trip);
        TripMember tripMemberOld = tripMember.getTrip()
                .getTripMembers()
                .stream()
                .filter(t -> t.getUser().getEmail().equals(acceptedTrip.getTripMemberDTO().getEmail()))
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

    public List<NotificationDTO> getNotificationsByEmail(String email) {
        List<Notification> notificationList = notificationRepository.getAllNotificationByEmail(email);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification :
                notificationList
        ) {
            notificationDTOList.add(notificationMapper.convertNotificationToNotificationDTO(notification));
        }
        return notificationDTOList;
    }

    public void deactivateNotification(Long id) {
        Notification notification = notificationRepository.getNotificationByID(id);
        notification.setIsActive(false);
        notificationRepository.updateNotification(notification);
    }

    public NotificationDTO getNotificationByID(Long notification_id) {
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        NotificationDTO notificationDTO = notificationMapper.convertNotificationToNotificationDTO(notification);
        return notificationDTO;
    }
}
