package lt.fivethreads.entities;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "TRIP")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Trip.findAll", query = "select t from Trip as t"),
        @NamedQuery(name = "Trip.findByOrganizer", query = "select tr from Trip as tr " +
                "JOIN FETCH tr.organizer as m " +
                "WHERE m.email=:organizer_email"),
        @NamedQuery(name = "Trip.findUserEmail", query = "select tr from Trip as tr " +
                "JOIN FETCH tr.tripMembers as m JOIN FETCH m.user as u " +
                "WHERE u.email=:user_email")
})
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Is flexible cannot be null.")
    private Boolean isFlexible;
    @NotNull(message="Start date cannot be null.")
    @Column(name="START_DATE")
    private Date startDate;

    @NotNull(message="Finish date cannot be null.")
    @Column(name="FINISH_DATE")
    private Date finishDate;

    @NotNull(message="Status cannot be null.")
    @Column(name="STATUS")
    private TripStatus tripStatus;

    @NotNull(message="Arrival cannot be null.")
    @Column(name="ARRIVAL")
    private String arrival;

    @NotNull(message="Organizer cannot be null.")
    @OneToOne
    @JoinColumn(name="organizer_ID")
    private User organizer;

    @NotNull(message="Departure cannot be null.")
    @Column(name="DEPARTURE")
    private String departure;

    @OneToMany(mappedBy = "trip")
    private List<TripMember> tripMembers = new ArrayList<>();

}
