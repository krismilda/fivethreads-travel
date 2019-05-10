package lt.fivethreads.entities.request.Notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.request.AccommodationDTO;
import lt.fivethreads.entities.request.CarTicketDTO;
import lt.fivethreads.entities.request.FlightTicketDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class NotificationInformationChanged {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull (message = "IsActive cannot be null.")
    private Boolean isActive;
    private String notificationType;
    @NotNull (message = "Name cannot be null.")
    private String name;
    @DateTimeFormat
    @NotNull (message = "Created Date cannot be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "CET")

    private Date created_date;
    @NotNull (message = "Trip ID cannot be null.")
    private Long trip_id;
    @NotNull (message = "Trip start date cannot be null.")
    private Date startDate;
    @NotNull (message = "Trip finish date cannot be null.")
    private Date finishDate;
    @NotNull (message = "Trip arrival cannot be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private String arrival;
    @NotNull (message = "Trip departure cannot be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private String departure;
    @NotNull (message = "Organizer cannot be null.")
    private UserInformationDTO organizer;
    @NotNull (message = "IsFlightTickedNeeded cannot be null.")
    private Boolean isFlightTickedNeeded;
    private FlightTicketDTO flightTicketDTO;
    @NotNull (message = "IsAccommodationNeeded cannot be null.")
    private Boolean isAccommodationNeeded;
    private AccommodationDTO accommodationDTO;
    @NotNull (message = "IsCarNeeded cannot be null.")
    private Boolean isCarNeeded;
    private CarTicketDTO carTicketDTO;
    private List<UserInformationDTO> otherTripMembers;
}
