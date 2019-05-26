package lt.fivethreads.services;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.*;
import lt.fivethreads.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CreateNotificationServiceImplementation implements CreateNotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AddressMapper addressMapper;

    public Notification createNotificationFullInfo(TripMember tripMember, String name) {
        Notification notification = new Notification();
        notification.setName(name);
        notification.setIsAnswered(Boolean.FALSE);
        notification.setIsActive(Boolean.TRUE);
        notification.setCreated_date(new Date());
        TripHistory tripHistory = new TripHistory();
        tripHistory.setStartDate(tripMember.getTrip().getStartDate());
        tripHistory.setFinishDate(tripMember.getTrip().getFinishDate());
        tripHistory.setArrival(addressMapper.copyInstance(tripMember.getTrip().getArrival()));
        tripHistory.setDeparture(addressMapper.copyInstance(tripMember.getTrip().getDeparture()));
        tripHistory.setOrganizer(tripMember.getTrip().getOrganizer());
        tripHistory.setIsFlightTickedNeeded(tripMember.getIsFlightTickedNeeded());
        tripHistory.setIsCarNeeded(tripMember.getIsCarNeeded());
        tripHistory.setPurpose(tripMember.getTrip().getPurpose());
        tripHistory.setIsAccommodationNeeded(tripMember.getIsAccommodationNeeded());
        if (tripHistory.getIsAccommodationNeeded()) {
            tripHistory.setAccommodationStart(tripMember.getTripAccommodation().getAccommodationStart());
            tripHistory.setAccommodationFinish(tripMember.getTripAccommodation().getAccommodationFinish());
            if(tripMember.getTripAccommodation().getPrice() != null)
            tripHistory.setAccommodationPrice(tripMember.getTripAccommodation().getPrice());
        }
        if (tripHistory.getIsCarNeeded()) {
            tripHistory.setCarRentStart(tripMember.getCarTicket().getCarRentStart());
            tripHistory.setCarRentFinish(tripMember.getCarTicket().getCarRentFinish());
            if(tripMember.getCarTicket().getPrice()!=null){
                tripHistory.setCarPrice(tripMember.getCarTicket().getPrice());
            }
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
        notification.setTripHistory(tripHistory);
        notification.getTripHistory().setTripID(tripMember.getTrip().getId());
        notification.getTripHistory().setTripMembers(tripMemberHistoryList);
        return notification;
    }

    public void createNotificationsForApproval(Trip trip, String name) {
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            this.createNotificationForApprovalTripMember(tripMember, name);
        }
    }

    public void createNotificationForApprovalTripMember(TripMember tripMember, String name) {
        Notification notification = createNotificationFullInfo(tripMember, name);
        notification.setNotificationType(NotificationType.ForApproval);
        notificationRepository.saveNotification(notification);
    }

    public void createNotificationInformationChanged(TripMember tripMember, String name) {
        Notification notification = createNotificationFullInfo(tripMember, name);
        notification.setNotificationType(NotificationType.InformationChanged);
        notificationRepository.saveNotification(notification);
    }

    public void createNotificationCancellation(TripCancellation tripCancellation, String name) {
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.Cancelled);
        notification.setIsActive(true);
        notification.setCreated_date(new Date());
        notification.setUser(tripCancellation.getTripMember().getUser());
        notification.setName(name);
        notification.setReason(tripCancellation.getReason());
        Long tripID = tripCancellation.getTripMember().getTrip().getId();
        String userEmail = tripCancellation.getTripMember().getUser().getEmail();
        Notification notification1 = notificationRepository.getAllUserNotificationByEmail(userEmail)
                .stream()
                .filter(e -> e.getTripHistory().getTripID().equals(tripID))
                .findAny()
                .orElse(null);
        notification.setTripHistory(notification1.getTripHistory());
        notificationRepository.saveNotification(notification);
    }

    public void createNotificationForApproval(TripMember tripMember, String name) {
        Notification notification = createNotificationFullInfo(tripMember, name);
        notification.setNotificationType(NotificationType.Approved);
        notificationRepository.saveNotification(notification);
    }

    public void createNotificationDeleted(TripMember tripMember, String name){
        Notification notification = createNotificationFullInfo(tripMember, name);
        notification.setNotificationType(NotificationType.Deleted);
        notificationRepository.saveNotification(notification);
    }

}
