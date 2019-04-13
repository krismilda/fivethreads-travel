package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripHistory;
import lt.fivethreads.entities.TripMemberHistory;
import lt.fivethreads.entities.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class NotificationDTO {
    @NotNull (message = "ID cannot be null.")
    private Long id;
    @NotNull (message = "IsActive cannot be null.")
    private Boolean isActive;
    @NotNull (message = "Name cannot be null.")
    private String name;
    @DateTimeFormat
    @NotNull (message = "Created Date cannot be null.")
    private Date created_date;
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
    @NotNull (message = "Organizer cannot be null.")
    private NotificationTripMemberDTO organizer;
    @NotNull (message = "IsFlightTickedNeeded cannot be null.")
    private Boolean isFlightTickedNeeded;
    @NotNull (message = "IsAccommodationNeeded cannot be null.")
    private Boolean isAccommodationNeeded;
    @NotNull (message = "IsCarNeeded cannot be null.")
    private Boolean isCarNeeded;
    private List<NotificationTripMemberDTO> otherTripMembers;
    private double flightPrice;
    private double accommodationPrice;
    @DateTimeFormat
    private Date accommodationStart;
    @DateTimeFormat
    private Date accommodationFinish;
    private double carPrice;
    @DateTimeFormat
    private Date carRentStart;
    @DateTimeFormat
    private Date carRentFinish;
}
