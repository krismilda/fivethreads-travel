package lt.fivethreads.mapper;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.TripMemberHistory;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.entities.request.Notifications.*;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.AddressService;
import lt.fivethreads.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    AddressService addressService;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    FileService fileService;
    public NotificationForApprovalDTO convertNotificationForApprovalToNotificationDTO(Notification notification) {
        NotificationForApprovalDTO notificationForApprovalDTO = new NotificationForApprovalDTO();
        notificationForApprovalDTO.setId(notification.getId());
        notificationForApprovalDTO.setIsAnswered(notification.getIsAnswered());
        notificationForApprovalDTO.setIsActive(notification.getIsActive());
        notificationForApprovalDTO.setNotificationType(notification.getNotificationType().toString());
        notificationForApprovalDTO.setName(notification.getName());
        notificationForApprovalDTO.setCreated_date(notification.getCreated_date());
        notificationForApprovalDTO.setTrip_id(notification.getTripHistory().getTripID());
        notificationForApprovalDTO.setStartDate(notification.getTripHistory().getStartDate());
        notificationForApprovalDTO.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationForApprovalDTO.setArrival(arrival);
        notificationForApprovalDTO.setDeparture(departure);
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        notificationUserDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        notificationUserDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        notificationUserDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationForApprovalDTO.setOrganizer(notificationUserDTO);
        notificationForApprovalDTO.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationForApprovalDTO.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationForApprovalDTO.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        if (notificationForApprovalDTO.getIsAccommodationNeeded()) {
            AccommodationDTOWithoutFiles accommodationDTO = new AccommodationDTOWithoutFiles();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            notificationForApprovalDTO.setAccommodationDTO(accommodationDTO);
        }
        if (notificationForApprovalDTO.getIsCarNeeded()) {
            CarTicketDTOWithoutFiles carTicketDTO = new CarTicketDTOWithoutFiles();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            notificationForApprovalDTO.setCarTicketDTO(carTicketDTO);
        }
        notificationForApprovalDTO.setNotificationType(notification.getNotificationType().toString());
        List<NotificationUserDTO> otherTripMembers = new ArrayList<>();
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
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

    public NotificationInformationChanged convertNotificationToNotificationInformationChangedDTO(Notification notification) {
        NotificationInformationChanged notificationInformationChanged = new NotificationInformationChanged();
        notificationInformationChanged.setId(notification.getId());
        notificationInformationChanged.setNotificationType(notification.getNotificationType().toString());
        notificationInformationChanged.setIsActive(notification.getIsActive());
        notificationInformationChanged.setName(notification.getName());
        notificationInformationChanged.setCreated_date(notification.getCreated_date());
        notificationInformationChanged.setTrip_id(notification.getTripHistory().getTripID());
        notificationInformationChanged.setStartDate(notification.getTripHistory().getStartDate());
        notificationInformationChanged.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationInformationChanged.setArrival(arrival);
        notificationInformationChanged.setDeparture(departure);
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
        if (notificationInformationChanged.getIsAccommodationNeeded()) {
            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            accommodationDTO.setPrice(notification.getTripHistory().getAccommodationPrice());
            if (tripMember.getTripAccommodation().getFile() != null) {
                accommodationDTO.setFiles(tripMember.getTripAccommodation()
                        .getFile()
                        .stream()
                        .map(e -> fileService.getFileById(e.getId()))
                        .collect(Collectors.toList()));
            }
            notificationInformationChanged.setAccommodationDTO(accommodationDTO);
        }
        if (notificationInformationChanged.getIsCarNeeded()) {
            CarTicketDTO carTicketDTO = new CarTicketDTO();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            carTicketDTO.setPrice(notification.getTripHistory().getCarPrice());
            if (tripMember.getCarTicket().getFile() != null) {
                carTicketDTO.setFiles(tripMember.getCarTicket()
                        .getFile()
                        .stream()
                        .map(e -> fileService.getFileById(e.getId()))
                        .collect(Collectors.toList()));
            }
            notificationInformationChanged.setCarTicketDTO(carTicketDTO);
        }
        if (notificationInformationChanged.getIsFlightTickedNeeded()) {
            FlightTicketDTO flightTicketDTO = new FlightTicketDTO();
            flightTicketDTO.setPrice(notification.getTripHistory().getFlightPrice());
            if(tripMember.getFlightTicket().getFile()!=null) {
                flightTicketDTO.setFiles(tripMember.getFlightTicket()
                        .getFile()
                        .stream()
                        .map(e -> fileService.getFileById(e.getId()))
                        .collect(Collectors.toList()));
            }
            notificationInformationChanged.setFlightTicketDTO(flightTicketDTO);
        }
        notificationInformationChanged.setNotificationType(notification.getNotificationType().toString());
        List<NotificationUserDTO> otherTripMembers = new ArrayList<>();
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
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

    public NotificationApproved convertNotificationToNotificationApprovedDTO(Notification notification) {
        NotificationApproved notificationApproved = new NotificationApproved();
        notificationApproved.setId(notification.getId());
        notificationApproved.setNotificationType(notification.getNotificationType().toString());
        notificationApproved.setIsActive(notification.getIsActive());
        notificationApproved.setName(notification.getName());
        notificationApproved.setCreated_date(notification.getCreated_date());
        notificationApproved.setTrip_id(notification.getTripHistory().getTripID());
        notificationApproved.setStartDate(notification.getTripHistory().getStartDate());
        notificationApproved.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationApproved.setArrival(arrival);
        notificationApproved.setDeparture(departure);
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getUser().getEmail());
        notificationUserDTO.setFirstName(notification.getUser().getFirstname());
        notificationUserDTO.setLastName(notification.getUser().getLastName());
        notificationUserDTO.setPhone(notification.getUser().getPhone());
        notificationApproved.setTripMember(notificationUserDTO);
        notificationApproved.setIsAccommodationNeeded(notification.getTripHistory().getIsAccommodationNeeded());
        notificationApproved.setIsCarNeeded(notification.getTripHistory().getIsCarNeeded());
        notificationApproved.setIsFlightTickedNeeded(notification.getTripHistory().getIsFlightTickedNeeded());
        if (notificationApproved.getIsAccommodationNeeded()) {
            AccommodationDTOWithoutFiles accommodationDTO = new AccommodationDTOWithoutFiles();
            accommodationDTO.setAccommodationStart(notification.getTripHistory().getAccommodationStart());
            accommodationDTO.setAccommodationFinish(notification.getTripHistory().getAccommodationFinish());
            notificationApproved.setAccommodationDTO(accommodationDTO);
        }
        if (notificationApproved.getIsCarNeeded()) {
            CarTicketDTOWithoutFiles carTicketDTO = new CarTicketDTOWithoutFiles();
            carTicketDTO.setCarRentStart(notification.getTripHistory().getCarRentStart());
            carTicketDTO.setCarRentFinish(notification.getTripHistory().getCarRentFinish());
            notificationApproved.setCarTicketDTO(carTicketDTO);
        }
        notificationApproved.setNotificationType(notification.getNotificationType().toString());
        return notificationApproved;
    }

    public NotificationCancelled convertNotificationToNotificationCancelled(Notification notification) {
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
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationCancelled.setArrival(arrival);
        notificationCancelled.setDeparture(departure);
        return notificationCancelled;
    }

    public NotificationTripDeleted convertNotificationToNotificationTripDeleted(Notification notification){
        NotificationTripDeleted notificationTripDeleted = new NotificationTripDeleted();
        notificationTripDeleted.setId(notification.getId());
        notificationTripDeleted.setNotificationType(notification.getNotificationType().toString());
        notificationTripDeleted.setIsActive(notification.getIsActive());
        notificationTripDeleted.setName(notification.getName());
        NotificationUserDTO notificationUserDTO = new NotificationUserDTO();
        notificationUserDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        notificationUserDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        notificationUserDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        notificationUserDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationTripDeleted.setOrganizer(notificationUserDTO);
        notificationTripDeleted.setTrip_id(notification.getTripHistory().getTripID());
        notificationTripDeleted.setStartDate(notification.getTripHistory().getStartDate());
        notificationTripDeleted.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationTripDeleted.setArrival(arrival);
        notificationTripDeleted.setDeparture(departure);
        return notificationTripDeleted;
    }
    public NotificationListDTO convertNotificationToNotificationListDTO(Notification notification){
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        notificationListDTO.setId(notification.getId());
        notificationListDTO.setCreated_date(notification.getCreated_date());
        notificationListDTO.setIsActive(notification.getIsActive());
        notificationListDTO.setName(notification.getName());
        notificationListDTO.setIsAnswered(notification.getIsAnswered());
        notificationListDTO.setNotificationType(notification.getNotificationType().toString());
        return  notificationListDTO;
    }
}
