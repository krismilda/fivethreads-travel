package lt.fivethreads.entities.request.Notifications;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class NotificationListDTO {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull (message = "IsActive cannot be null.")
    private Boolean isActive;
    @NotNull(message = "Notification type cannot be empty")
    private String notificationType;
    @NotNull (message = "Name cannot be null.")
    private String name;
    @DateTimeFormat
    @NotNull (message = "Created Date cannot be null.")
    private Date created_date;
}
