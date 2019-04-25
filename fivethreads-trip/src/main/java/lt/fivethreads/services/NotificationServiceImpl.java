package lt.fivethreads.services;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AcceptedTrip;
import lt.fivethreads.entities.request.CancelledTrip;
import lt.fivethreads.entities.request.Notifications.*;
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

import java.util.ArrayList;
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

    @Autowired
    CreateNotificationService createNotificationService;

    public void tripAccepted(AcceptedTrip acceptedTrip) {
        TripMember tripMember = tripMemberMapper.convertTripMemberDTOtoTripMember(acceptedTrip.getTripMemberDTO());
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
        createNotificationService.createNotificationForApproval(tripMember, "Trip was approved.");
    }

    public void tripCancelled(CancelledTrip cancelledTrip) {
        TripCancellation tripCancellation = tripMapper.convertCancelledTripToObject(cancelledTrip);
        tripCancellation.getTripMember().setTripCancellation(tripCancellation);
        tripCancellation.getTripMember().setTripAcceptance(TripAcceptance.CANCELLED);
        tripMemberRepository.addCancellation(tripCancellation);
        createNotificationService.createNotificationForCancellation(tripCancellation, "Trip was cancelled.");
    }

    public List<NotificationListDTO> getUserNotification(String email) {
        List<Notification> notificationList = notificationRepository.getAllUserNotificationByEmail(email);
        List<NotificationListDTO> notificationListDTOS = new ArrayList<>();
        for (Notification notification :
                notificationList
        ) {
            notificationListDTOS.add(notificationMapper.convertNotificationToNotificationListDTO(notification));
        }
        return notificationListDTOS;
    }

    public List<NotificationListDTO> getOrganizerNotification(String email) {
        List<Notification> notificationList = notificationRepository.getAllOrganizerNotificationByEmail(email);
        List<NotificationListDTO> notificationListDTOS = new ArrayList<>();
        for (Notification notification :
                notificationList
        ) {
            notificationListDTOS.add(notificationMapper.convertNotificationToNotificationListDTO(notification));
        }
        return notificationListDTOS;
    }

    public void deactivateNotification(Long id) {
        Notification notification = notificationRepository.getNotificationByID(id);
        notification.setIsActive(false);
        notificationRepository.updateNotification(notification);
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
