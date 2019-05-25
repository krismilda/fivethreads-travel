package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
public class TripAccommodation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="unique_id")
    private String uniqueID;


    @NotNull(message = "TripAccommodation Start cannot be null.")
    private Date accommodationStart;

    @NotNull(message = "TripAccommodation Finish cannot be null.")
    private Date accommodationFinish;

    @OneToOne
    @JoinColumn(name="ROOM_ID")
    private Room room;


    private AccommodationType accommodationType;

    @Column(name = "hotelName")
    private String hotelName;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="hotelAddress")
    private Address hotelAddress;

    @Column(name = "price")
    private Double price;

    @OneToOne(mappedBy = "tripAccommodation")
    @NotNull(message = "TripMember cannot be null")
    @JoinColumn(name="TRIPMEMBER_ID")
    private TripMember tripMember;

    @OneToMany
    @JoinColumns({
            @JoinColumn(
                    name = "tickect_id",
                    referencedColumnName = "unique_id")
    })
    private List<File> file =  new ArrayList<>();

}
