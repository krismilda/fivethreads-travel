package lt.fivethreads.entities.request.Notifications;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationListFullDTO {
    long count;
    int page;
    List<NotificationListDTO> notificationList;
}
