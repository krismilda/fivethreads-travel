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
        notificationForApprovalDTO.setPurpose(notification.getTripHistory().getPurpose());
        notificationForApprovalDTO.setCreated_date(notification.getCreated_date());
        notificationForApprovalDTO.setTrip_id(notification.getTripHistory().getTripID());
        notificationForApprovalDTO.setStartDate(notification.getTripHistory().getStartDate());
        notificationForApprovalDTO.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationForApprovalDTO.setArrival(arrival);
        notificationForApprovalDTO.setDeparture(departure);
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        userInformationDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        userInformationDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        userInformationDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationForApprovalDTO.setOrganizer(userInformationDTO);
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
        List<UserInformationDTO> otherTripMembers = new ArrayList<>();
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
        ) {
            UserInformationDTO userInformationDTO1 = new UserInformationDTO();
            userInformationDTO1.setEmail(tripMemberHistory.getEmail());
            userInformationDTO1.setFirstName(tripMemberHistory.getFirstname());
            userInformationDTO1.setLastName(tripMemberHistory.getLastName());
            userInformationDTO1.setPhone(tripMemberHistory.getPhone());
            otherTripMembers.add(userInformationDTO1);
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
        notificationInformationChanged.setPurpose(notification.getTripHistory().getPurpose());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationInformationChanged.setArrival(arrival);
        notificationInformationChanged.setDeparture(departure);
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        userInformationDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        userInformationDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        userInformationDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationInformationChanged.setOrganizer(userInformationDTO);
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
        List<UserInformationDTO> otherTripMembers = new ArrayList<>();
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
        ) {
            UserInformationDTO userInformationDTO1 = new UserInformationDTO();
            userInformationDTO1.setEmail(tripMemberHistory.getEmail());
            userInformationDTO1.setFirstName(tripMemberHistory.getFirstname());
            userInformationDTO1.setLastName(tripMemberHistory.getLastName());
            userInformationDTO1.setPhone(tripMemberHistory.getPhone());
            otherTripMembers.add(userInformationDTO1);
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
        notificationApproved.setPurpose(notification.getTripHistory().getPurpose());
        notificationApproved.setFinishDate(notification.getTripHistory().getFinishDate());
        String arrival = addressService.getCombinedAddress(notification.getTripHistory().getArrival());
        String departure = addressService.getCombinedAddress(notification.getTripHistory().getDeparture());
        notificationApproved.setArrival(arrival);
        notificationApproved.setDeparture(departure);
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setEmail(notification.getUser().getEmail());
        userInformationDTO.setFirstName(notification.getUser().getFirstname());
        userInformationDTO.setLastName(notification.getUser().getLastName());
        userInformationDTO.setPhone(notification.getUser().getPhone());
        notificationApproved.setTripMember(userInformationDTO);
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
        notificationCancelled.setPurpose(notification.getTripHistory().getPurpose());
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setEmail(notification.getUser().getEmail());
        userInformationDTO.setFirstName(notification.getUser().getFirstname());
        userInformationDTO.setLastName(notification.getUser().getLastName());
        userInformationDTO.setPhone(notification.getUser().getPhone());
        notificationCancelled.setTripMember(userInformationDTO);
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
        notificationTripDeleted.setPurpose(notification.getTripHistory().getPurpose());
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setEmail(notification.getTripHistory().getOrganizer().getEmail());
        userInformationDTO.setFirstName(notification.getTripHistory().getOrganizer().getFirstname());
        userInformationDTO.setLastName(notification.getTripHistory().getOrganizer().getLastName());
        userInformationDTO.setPhone(notification.getTripHistory().getOrganizer().getPhone());
        notificationTripDeleted.setOrganizer(userInformationDTO);
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
