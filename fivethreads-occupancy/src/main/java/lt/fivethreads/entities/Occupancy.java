package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "OCCUPANCY")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name ="Occupancy.FindAll", query = "select o from Occupancy as o"),
        @NamedQuery(name ="Occupancy.FindByTrip", query = "select o from Occupancy as o " +
                "WHERE o.trip =:trip_ID"),
        @NamedQuery(name ="Occupancy.FindByUser", query = "select o from Occupancy as o " +
                "WHERE o.user =:user_ID"),
        @NamedQuery(name = "Occupancy.FindByApartment", query = "select o from Occupancy as o " +
                "JOIN FETCH o.room as r " +
                "WHERE r.apartment =: apartment_ID")

})
public class Occupancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Room cannot be null.")
    @OneToOne
    @JoinColumn(name = "room_ID")
    private Room room;

    @NotNull(message = "User cannot be null")
    @OneToOne
    @JoinColumn(name = "user_ID")
    private User user;

    @NotNull(message = "Trip cannot be null")
    @ManyToOne(targetEntity = Trip.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_ID")
    private Trip trip;

    @NotNull(message="Start date cannot be null.")
    @Column(name="START_DATE")
    private Date startDate;

    @NotNull(message="Finish date cannot be null.")
    @Column(name="FINISH_DATE")
    private Date finishDate;

}
