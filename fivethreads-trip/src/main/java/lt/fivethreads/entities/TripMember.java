package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TRIP_MEMBER")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "TripMember.findByTripIDEmail", query = "SELECT e FROM TripMember e " +
        "JOIN FETCH e.user " +
        "JOIN FETCH e.trip " +
        "WHERE e.trip.id LIKE :tripID AND e.user.id=:user_id"),
        @NamedQuery(name = "TripMember.findByFlightFileID", query = "SELECT e FROM TripMember e " +
                "JOIN FETCH e.flightTicket f " +
                "JOIN FETCH f.file h " +
                "WHERE h.id LIKE :fileID"),
        @NamedQuery(name = "TripMember.findByAccommodationFileID", query = "SELECT e FROM TripMember e " +
                "JOIN FETCH e.tripAccommodation f " +
                "JOIN FETCH f.file h " +
                "WHERE h.id LIKE :fileID"),
        @NamedQuery(name = "TripMember.findByCarFileID", query = "SELECT e FROM TripMember e " +
                "JOIN FETCH e.carTicket f " +
                "JOIN FETCH f.file h " +
                "WHERE h.id LIKE :fileID")

})
public class TripMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private User user;

    private TripAcceptance tripAcceptance;

    @OneToOne(mappedBy = "tripMember")
    private TripCancellation tripCancellation;

    private Boolean isFlightTickedNeeded;

    private Boolean isAccommodationNeeded;

    private Boolean isCarNeeded;

    @ManyToOne
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    @OneToOne
    @JoinColumn(name="FLIGHT_TICKET_ID")
    private FlightTicket flightTicket;

    @OneToOne
    @JoinColumn(name="accommodation_ID")
    private TripAccommodation tripAccommodation;

    @OneToOne
    @JoinColumn(name="car_id")
    private CarTicket carTicket;
}
