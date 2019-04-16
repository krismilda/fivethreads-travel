package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TripAccommodation")
@Getter
@Setter
public class TripAccommodation implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="unique_id")
    private String uniqueID;

    private Date accommodationStart;

    private Date accommodationFinish;

    @OneToOne
    @JoinColumn(name="APARTMENT_ID")
    private Apartment apartment;

    private AccommodationType accommodationType;

    private String hotelName;

    private String hotelAddress;

    private double price;

    @OneToOne(mappedBy = "tripAccommodation")
    private TripMember tripMember;

    @OneToMany
    @JoinColumns({
            @JoinColumn(
                    name = "tickect_id",
                    referencedColumnName = "unique_id")
    })
    private List<File> file =  new ArrayList<>();

}
