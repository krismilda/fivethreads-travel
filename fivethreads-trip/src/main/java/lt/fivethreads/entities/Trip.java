package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "TRIP")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Trip.findAll", query = "select t from Trip as t")
})
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="FINISH_DATE")
    private Date finishDate;

    @Column(name="STATUS")
    private TripStatus tripStatus;

    @Column(name="ARRIVAL")
    private String arrival;

    @Column(name="DEPARTURE")
    private String departure;

    @OneToMany(mappedBy = "trip")
    private List<TripMember> tripMembers = new ArrayList<>();

}
