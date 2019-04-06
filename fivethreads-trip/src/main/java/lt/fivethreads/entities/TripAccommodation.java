package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TripAccommodation")
@Getter
@Setter
public class TripAccommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Date accommodationStart;

    private Date accommodationFinish;

    @OneToOne
    @JoinColumn(name="APARTMENT_ID")
    private Apartment apartment;

    private AccommodationType accommodationType;

    private String hotelName;

    private String hotelAddress;

    private double price;

    @OneToOne
    @JoinColumn(name="TRIPMEMBER_ID")
    private TripMember tripMember;

    @OneToMany
    @JoinColumn(name="tickect_id")
    private List<File> file;

}
