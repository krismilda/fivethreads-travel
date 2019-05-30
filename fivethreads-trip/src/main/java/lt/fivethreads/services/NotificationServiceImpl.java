package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.Notifications.*;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.exception.WrongNotificationTypeOrID;
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

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    CreateNotificationService createNotificationService;

    public TripMemberDTO tripAccepted(AcceptedTrip acceptedTrip, String email) {
        TripMember tripMember = tripMapper.convertAcceptedTripToTripMember(acceptedTrip, email);
        Notification notification = notificationRepository.getAllUserNotificationByEmail(email)
                .stream()
                .filter(e->e.getTripHistory().getTripID().equals(acceptedTrip.getTripID()))
                .findFirst()
                .orElseThrow(() -> new WrongTripData("Notification does not exist"));
        notification.setIsAnswered(true);
        notificationRepository.updateNotification(notification);
        Trip trip = tripRepository.findByID(acceptedTrip.getTripID());
        if (trip == null) {
            throw new WrongTripData("Trip ID does not exist.");
        }
        tripMember.setTrip(trip);
        TripMember tripMemberOld = tripMember.getTrip()
                .getTripMembers()
                .stream()
                .filter(t -> t.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new WrongTripData("User's email is wrong."));
        tripMember.setId(tripMemberOld.getId());
        tripValidation.validateTripMember(tripMember);
        tripMember.setTripAcceptance(TripAcceptance.ACCEPTED);
        tripMemberRepository.updateTripMember(tripMember);
        createNotificationService.createNotificationForApproval(tripMember, "Trip was approved.");
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember);
    }

    public TripMemberDTO tripCancelled(CancelledTrip cancelledTrip, String email) {
        TripCancellation tripCancellation = tripMapper.convertCancelledTripToObject(cancelledTrip, email);
        Notification notification = notificationRepository.getAllUserNotificationByEmail(email)
                .stream()
                .filter(e->e.getTripHistory().getTripID().equals(cancelledTrip.getTripID()))
                .findFirst()
                .orElseThrow(() -> new WrongTripData("Notification does not exist"));
        notification.setIsAnswered(true);
        notificationRepository.updateNotification(notification);
        tripCancellation.getTripMember().setTripCancellation(tripCancellation);
        tripCancellation.getTripMember().setTripAcceptance(TripAcceptance.CANCELLED);
        tripMemberRepository.addCancellation(tripCancellation);
        createNotificationService.createNotificationCancellation(tripCancellation, "Trip was cancelled.");
        return tripMemberMapper.convertTripMemberToTripMemberDTO(tripCancellation.getTripMember());
    }

    public NotificationListFullDTO getUserNotification(String email, int page, int amount) {
        List<Notification> notificationList = notificationRepository.getAllUserNotificationByEmailPage(email, (page-1), amount);
        List<NotificationListDTO> notificationListDTOS = new ArrayList<>();
        for (Notification notification :
                notificationList
        ) {
            notificationListDTOS.add(notificationMapper.convertNotificationToNotificationListDTO(notification));
        }
        NotificationListFullDTO notificationListFullDTO = new NotificationListFullDTO();
        long count = notificationRepository.getCountNotificationByEmailUser(email);
        notificationListFullDTO.setCount(count);
        notificationListFullDTO.setPage(page);
        notificationListFullDTO.setNotificationList(notificationListDTOS);
        return notificationListFullDTO;
    }

    public NotificationListFullDTO getOrganizerNotification(String email, int page, int amount) {
        List<Notification> notificationList = notificationRepository.getAllOrganizerNotificationByEmailPage(email, (page-1), amount);
        List<NotificationListDTO> notificationListDTOS = new ArrayList<>();
        for (Notification notification :
                notificationList
        ) {
            notificationListDTOS.add(notificationMapper.convertNotificationToNotificationListDTO(notification));
        }
        NotificationListFullDTO notificationListFullDTO = new NotificationListFullDTO();
        long count = notificationRepository.getCountNotificationByEmailOrganizer(email);
        notificationListFullDTO.setCount(count);
        notificationListFullDTO.setPage(page);
        notificationListFullDTO.setNotificationList(notificationListDTOS);
        return notificationListFullDTO;
    }

    public NotificationListDTO deactivateNotification(Long id) {
        Notification notification = notificationRepository.getNotificationByID(id);
        notification.setIsActive(false);
        notificationRepository.updateNotification(notification);
        return notificationMapper.convertNotificationToNotificationListDTO(notification);
    }

    public NotificationForApprovalDTO getNotificationByIDForApproval(Long notification_id, String email){
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        if(!notification.getUser().getEmail().equals(email) || !notification.getNotificationType().toString().equals("ForApproval")){
            throw new WrongNotificationTypeOrID("Wrong notification ID or type.");
        }
        NotificationForApprovalDTO notificationForApprovalDTO = notificationMapper.convertNotificationForApprovalToNotificationDTO(notification);
        return notificationForApprovalDTO;
    }

    public NotificationApproved getNotificationByIDForApproved(Long notification_id, String email){
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        if(!notification.getTripHistory().getOrganizer().getEmail().equals(email) || !notification.getNotificationType().toString().equals("Approved")){
            throw new WrongNotificationTypeOrID("Wrong notification ID or type.");
        }
        NotificationApproved notificationApproved = notificationMapper.convertNotificationToNotificationApprovedDTO(notification);
        return  notificationApproved;
    }


    public NotificationCancelled getNotificationByIDForCancelled(Long notification_id, String email){
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        if(!notification.getTripHistory().getOrganizer().getEmail().equals(email) || !notification.getNotificationType().toString().equals("Cancelled")){
            throw new WrongNotificationTypeOrID("Wrong notification ID or type.");
        }
        NotificationCancelled notificationCancelled = notificationMapper.convertNotificationToNotificationCancelled(notification);
        return  notificationCancelled;
    }

    public NotificationInformationChanged getNotificationByIDForInformationChanged(Long notification_id, String email){
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        if(!notification.getUser().getEmail().equals(email) || !notification.getNotificationType().toString().equals("InformationChanged")){
            throw new WrongNotificationTypeOrID("Wrong notification ID or type.");
        }
        NotificationInformationChanged notificationInformationChanged = notificationMapper.convertNotificationToNotificationInformationChangedDTO(notification);
        return  notificationInformationChanged;
    }

    public NotificationTripDeleted getNotificationByIDDeleted(Long notification_id, String email){
        Notification notification = notificationRepository.getNotificationByID(notification_id);
        if(!notification.getUser().getEmail().equals(email) || !notification.getNotificationType().toString().equals("Deleted")){
            throw new WrongNotificationTypeOrID("Wrong notification ID or type.");
        }
        NotificationTripDeleted notificationTripDeleted = notificationMapper.convertNotificationToNotificationTripDeleted(notification);
        return  notificationTripDeleted;
    }
}
