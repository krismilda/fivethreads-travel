package lt.fivethreads.mapper;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.TripMemberHistory;
import lt.fivethreads.entities.request.NotificationDTO;
import lt.fivethreads.entities.request.NotificationTripMemberDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationMapper {

    public NotificationDTO convertNotificationToNotificationDTO(Notification notification){
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setIsActive(notification.getIsActive());
        notificationDTO.setName(notification.getName());
        notificationDTO.setCreated_date(notification.getCreated_date());
        notificationDTO.setTrip_id(notification.getTrip().getId());
        notificationDTO.setStartDate(notification.getTripHistory().getStartDate());
        notificationDTO.setFinishDate(notification.getTripHistory().getFinishDate());
        notificationDTO.setArrival(notification.getTripHistory().getArrival());
        notificationDTO.setDeparture(notification.getTripHistory().getDeparture());
        NotificationTripMemberDTO notificationTripMemberDTO = new NotificationTripMemberDTO();
        notificationTripMemberDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        notificationTripMemberDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        notificationTripMemberDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        notificationTripMemberDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationDTO.setOrganizer(notificationTripMemberDTO);
        notificationDTO.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationDTO.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationDTO.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        notificationDTO.setAccommodationPrice(notification.getTripHistory().getAccommodationPrice());
        notificationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
        notificationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
        notificationDTO.setCarPrice(notification.getTripHistory().getCarPrice());
        notificationDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
        notificationDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
        List<NotificationTripMemberDTO> otherTripMembers =new ArrayList<>();
        for (TripMemberHistory tripMemberHistory:notification.getTripHistory().getTripMembers()
             ) {
            NotificationTripMemberDTO notificationTripMemberDTO1 = new NotificationTripMemberDTO();
            notificationTripMemberDTO1.setEmail(tripMemberHistory.getEmail());
            notificationTripMemberDTO1.setFirstName(tripMemberHistory.getFirstname());
            notificationTripMemberDTO1.setLastName(tripMemberHistory.getLastName());
            notificationTripMemberDTO1.setPhone(tripMemberHistory.getPhone());
            otherTripMembers.add(notificationTripMemberDTO1);
        }
        notificationDTO.setOtherTripMembers(otherTripMembers);
        return notificationDTO;
    }
}
