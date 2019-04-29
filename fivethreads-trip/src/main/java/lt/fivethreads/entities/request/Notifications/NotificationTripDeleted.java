package lt.fivethreads.entities.request.Notifications;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class NotificationTripDeleted {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull (message = "IsActive cannot be null.")
    private Boolean isActive;
    private String notificationType;
    @NotNull (message = "Name cannot be null.")
    private String name;
    @NotNull (message = "Organizer cannot be null.")
    private NotificationUserDTO organizer;
    @NotNull (message = "Trip ID cannot be null.")
    private Long trip_id;
    @NotNull (message = "Trip start date cannot be null.")
    private Date startDate;
    @NotNull (message = "Trip finish date cannot be null.")
    private Date finishDate;
    @NotNull (message = "Trip arrival cannot be null.")
    private String arrival;
    @NotNull (message = "Trip departure cannot be null.")
    private String departure;
}
