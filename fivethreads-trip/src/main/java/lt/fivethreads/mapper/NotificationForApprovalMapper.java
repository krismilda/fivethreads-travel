package lt.fivethreads.mapper;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.entities.request.Notifications.*;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.repositories.TripRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationForApprovalMapper {

    @Autowired
    TripMemberRepository tripMemberRepository;

    public NotificationForApprovalDTO convertNotificationForApprovalToNotificationDTO(Notification notification){
        NotificationForApprovalDTO notificationForApprovalDTO = new NotificationForApprovalDTO();
        notificationForApprovalDTO.setId(notification.getId());
        notificationForApprovalDTO.setIsActive(notification.getIsActive());
        notificationForApprovalDTO.setNotificationType(notification.getNotificationType().toString());
        notificationForApprovalDTO.setName(notification.getName());
        notificationForApprovalDTO.setCreated_date(notification.getCreated_date());
        notificationForApprovalDTO.setTrip_id(notification.getTripHistory().getTripID());
        notificationForApprovalDTO.setStartDate(notification.getTripHistory().getStartDate());
        notificationForApprovalDTO.setFinishDate(notification.getTripHistory().getFinishDate());
        notificationForApprovalDTO.setArrival(notification.getTripHistory().getArrival());
        notificationForApprovalDTO.setDeparture(notification.getTripHistory().getDeparture());
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        notificationUserDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        notificationUserDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        notificationUserDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationForApprovalDTO.setOrganizer(notificationUserDTO);
        notificationForApprovalDTO.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationForApprovalDTO.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationForApprovalDTO.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        if(notificationForApprovalDTO.getIsAccommodationNeeded()){
            AccommodationDTOWithoutFiles accommodationDTO = new AccommodationDTOWithoutFiles();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            notificationForApprovalDTO.setAccommodationDTO(accommodationDTO);
        }
        if(notificationForApprovalDTO.getIsCarNeeded()){
            CarTicketDTOWithoutFiles carTicketDTO = new CarTicketDTOWithoutFiles();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            notificationForApprovalDTO.setCarTicketDTO(carTicketDTO);
        }
        notificationForApprovalDTO.setNotificationType(notification.getNotificationType().toString());
        List<NotificationUserDTO> otherTripMembers =new ArrayList<>();
        for (TripMemberHistory tripMemberHistory:notification.getTripHistory().getTripMembers()
             ) {
            NotificationUserDTO notificationUserDTO1 = new NotificationUserDTO();
            notificationUserDTO1.setEmail(tripMemberHistory.getEmail());
            notificationUserDTO1.setFirstName(tripMemberHistory.getFirstname());
            notificationUserDTO1.setLastName(tripMemberHistory.getLastName());
            notificationUserDTO1.setPhone(tripMemberHistory.getPhone());
            otherTripMembers.add(notificationUserDTO1);
        }
        notificationForApprovalDTO.setOtherTripMembers(otherTripMembers);
        return notificationForApprovalDTO;
    }
    public NotificationInformationChanged convertNotificationToNotificationInformationChangedDTO(Notification notification){
        NotificationInformationChanged notificationInformationChanged = new NotificationInformationChanged();
        notificationInformationChanged.setId(notification.getId());
        notificationInformationChanged.setNotificationType(notification.getNotificationType().toString());
        notificationInformationChanged.setIsActive(notification.getIsActive());
        notificationInformationChanged.setName(notification.getName());
        notificationInformationChanged.setCreated_date(notification.getCreated_date());
        notificationInformationChanged.setTrip_id(notification.getTripHistory().getTripID());
        notificationInformationChanged.setStartDate(notification.getTripHistory().getStartDate());
        notificationInformationChanged.setFinishDate(notification.getTripHistory().getFinishDate());
        notificationInformationChanged.setArrival(notification.getTripHistory().getArrival());
        notificationInformationChanged.setDeparture(notification.getTripHistory().getDeparture());
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        notificationUserDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        notificationUserDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        notificationUserDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationInformationChanged.setOrganizer(notificationUserDTO);
        notificationInformationChanged.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationInformationChanged.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationInformationChanged.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(notification.getTripHistory().getTripID(),
                notification.getUser().getEmail());
        if(notificationInformationChanged.getIsAccommodationNeeded()){
            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            accommodationDTO.setPrice(notification.getTripHistory().getAccommodationPrice());
            accommodationDTO.setFileID(tripMember.getTripAccommodation()
                    .getFile()
                    .stream()
                    .map(e -> e.getId())
                    .collect(Collectors.toList()));
            notificationInformationChanged.setAccommodationDTO(accommodationDTO);
        }
        if(notificationInformationChanged.getIsCarNeeded()){
            CarTicketDTO carTicketDTO = new CarTicketDTO();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            carTicketDTO.setPrice(notification.getTripHistory().getCarPrice());
            carTicketDTO.setFileID(tripMember.getCarTicket()
                    .getFile()
                    .stream()
                    .map(e -> e.getId())
                    .collect(Collectors.toList()));
            notificationInformationChanged.setCarTicketDTO(carTicketDTO);
        }
        if(notificationInformationChanged.getIsFlightTickedNeeded()){
            FlightTicketDTO flightTicketDTO = new FlightTicketDTO();
            flightTicketDTO.setPrice(notification.getTripHistory().getFlightPrice());
            flightTicketDTO.setFileID(tripMember.getFlightTicket()
                    .getFile()
                    .stream()
                    .map(e -> e.getId())
                    .collect(Collectors.toList()));
            notificationInformationChanged.setFlightTicketDTO(flightTicketDTO);
        }
        notificationInformationChanged.setNotificationType(notification.getNotificationType().toString());
        List<NotificationUserDTO> otherTripMembers =new ArrayList<>();
        for (TripMemberHistory tripMemberHistory:notification.getTripHistory().getTripMembers()
        ) {
            NotificationUserDTO notificationUserDTO1 = new NotificationUserDTO();
            notificationUserDTO1.setEmail(tripMemberHistory.getEmail());
            notificationUserDTO1.setFirstName(tripMemberHistory.getFirstname());
            notificationUserDTO1.setLastName(tripMemberHistory.getLastName());
            notificationUserDTO1.setPhone(tripMemberHistory.getPhone());
            otherTripMembers.add(notificationUserDTO1);
        }
        notificationInformationChanged.setOtherTripMembers(otherTripMembers);
        return notificationInformationChanged;
    }

    public NotificationApproved convertNotificationToNotificationApprovedDTO(Notification notification){
        NotificationApproved notificationApproved = new NotificationApproved();
        notificationApproved.setId(notification.getId());
        notificationApproved.setNotificationType(notification.getNotificationType().toString());
        notificationApproved.setIsActive(notification.getIsActive());
        notificationApproved.setName(notification.getName());
        notificationApproved.setCreated_date(notification.getCreated_date());
        notificationApproved.setTrip_id(notification.getTripHistory().getTripID());
        notificationApproved.setStartDate(notification.getTripHistory().getStartDate());
        notificationApproved.setFinishDate(notification.getTripHistory().getFinishDate());
        notificationApproved.setArrival(notification.getTripHistory().getArrival());
        notificationApproved.setDeparture(notification.getTripHistory().getDeparture());
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getUser().getEmail());
        notificationUserDTO.setFirstName(notification.getUser().getFirstname());
        notificationUserDTO.setLastName(notification.getUser().getLastName());
        notificationUserDTO.setPhone(notification.getUser().getPhone());
        notificationApproved.setTripMember(notificationUserDTO);
        notificationApproved.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationApproved.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationApproved.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        if(notificationApproved.getIsAccommodationNeeded()){
            AccommodationDTOWithoutFiles accommodationDTO = new AccommodationDTOWithoutFiles();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            notificationApproved.setAccommodationDTO(accommodationDTO);
        }
        if(notificationApproved.getIsCarNeeded()){
            CarTicketDTOWithoutFiles carTicketDTO = new CarTicketDTOWithoutFiles();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            notificationApproved.setCarTicketDTO(carTicketDTO);
        }
        notificationApproved.setNotificationType(notification.getNotificationType().toString());
        return notificationApproved;
    }

    public NotificationCancelled convertNotificationToNotificationCancelled(Notification notification){
        NotificationCancelled notificationCancelled = new NotificationCancelled();
        notificationCancelled.setId(notification.getId());
        notificationCancelled.setNotificationType(notification.getNotificationType().toString());
        notificationCancelled.setIsActive(notification.getIsActive());
        notificationCancelled.setName(notification.getName());
        notificationCancelled.setReason(notification.getReason());
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getUser().getEmail());
        notificationUserDTO.setFirstName(notification.getUser().getFirstname());
        notificationUserDTO.setLastName(notification.getUser().getLastName());
        notificationUserDTO.setPhone(notification.getUser().getPhone());
        notificationCancelled.setTripMember(notificationUserDTO);
        notificationCancelled.setTrip_id(notification.getTripHistory().getTripID());
        notificationCancelled.setStartDate(notification.getTripHistory().getStartDate());
        notificationCancelled.setFinishDate(notification.getTripHistory().getFinishDate());
        notificationCancelled.setArrival(notification.getTripHistory().getArrival());
        notificationCancelled.setDeparture(notification.getTripHistory().getDeparture());
        return notificationCancelled;
    }
}
