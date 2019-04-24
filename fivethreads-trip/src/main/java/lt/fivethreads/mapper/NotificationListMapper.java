package lt.fivethreads.mapper;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.request.Notifications.NotificationForApprovalDTO;
import lt.fivethreads.entities.request.Notifications.NotificationListDTO;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Component;

@Component
public class NotificationListMapper {

    public NotificationListDTO convertNotificationToNotificationListDTO(Notification notification){
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        notificationListDTO.setId(notification.getId());
        notificationListDTO.setCreated_date(notification.getCreated_date());
        notificationListDTO.setIsActive(notification.getIsActive());
        notificationListDTO.setName(notification.getName());
        notificationListDTO.setNotificationType(notification.getNotificationType().toString());
        return  notificationListDTO;
    }

}
