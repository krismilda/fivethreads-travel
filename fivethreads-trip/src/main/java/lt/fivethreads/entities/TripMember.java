package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TRIP_MEMBER")
@Getter
@Setter
public class TripMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private User user;

    private boolean accommodationNeeded;

    private TripAcceptance tripAcceptance;

    private boolean carNeeded;

    private Date carStart;

    private Date carFinish;

    private boolean flightTicketsNeeded;

    @OneToOne(mappedBy = "tripMember")
    private TripCancellation tripCancellation;

    @ManyToOne
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @OneToOne(mappedBy = "tripMember")
    private Accommodation accommodation;
}
