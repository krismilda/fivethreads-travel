package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TRIP_HISTORY")
@Getter
@Setter
public class TripHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long tripID;

    @NotNull(message="Start date cannot be null.")
    @Column(name="START_DATE")
    private Date startDate;

    @NotNull(message="Finish date cannot be null.")
    @Column(name="FINISH_DATE")
    private Date finishDate;

    @NotNull(message="Arrival cannot be null.")
    @Column(name="ARRIVAL")
    private String arrival;

    @NotNull(message="Departure cannot be null.")
    @Column(name="DEPARTURE")
    private String departure;

    @NotNull(message="Organizer cannot be null.")
    @OneToOne
    @JoinColumn(name="organizer_ID")
    private User organizer;

    private Boolean isFlightTickedNeeded;

    private Boolean isAccommodationNeeded;

    private Boolean isCarNeeded;

    @OneToMany(mappedBy = "tripHistory")
    private List<TripMemberHistory> tripMembers;

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
