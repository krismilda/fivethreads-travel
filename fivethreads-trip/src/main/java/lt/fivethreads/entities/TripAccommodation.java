package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TripAccommodation")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name ="TripAccommodation.FindAll", query = "select ta from TripAccommodation as ta"),
        @NamedQuery(name ="TripAccommodation.FindByTrip", query = "select ta from TripAccommodation as ta " +
                "JOIN FETCH ta.tripMember as tm WHERE tm.trip =:trip_ID"),
        @NamedQuery(name ="TripAccommodation.FindByUser", query = "select ta from TripAccommodation as ta " +
                "WHERE ta.tripMember =:user_ID"),
        @NamedQuery(name = "TripAccommodation.FindByApartment", query = "select ta from TripAccommodation as ta " +
                "JOIN FETCH ta.room as r " +
                "WHERE r.apartment =: apartment_ID")
})
public class TripAccommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "TripAccommodation Start cannot be null.")
    private Date accommodationStart;

    @NotNull(message = "TripAccommodation Finish cannot be null.")
    private Date accommodationFinish;

    @OneToOne
    @JoinColumn(name="ROOM_ID")
    private Room room;

    @NotNull(message = "AccommodationType cannot be null.")
    private AccommodationType accommodationType;

    @Column(name = "hotelName")
    private String hotelName;
    @Column(name = "hotelAddress")
    private String hotelAddress;

    @Column(name = "price")
    private Double price;

    @NotNull(message = "TripMember cannot be null")
    @OneToOne
    @JoinColumn(name="TRIPMEMBER_ID")
    private TripMember tripMember;

    @OneToMany
    @JoinColumn(name="tickect_id")
    private List<File> file;

}
