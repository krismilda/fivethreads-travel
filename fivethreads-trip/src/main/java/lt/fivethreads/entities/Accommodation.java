package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Accommodation")
@Getter
@Setter
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Date accommodationStart;

    private Date accommodationFinish;

    private AccommodationType accommodationType;

    private String hotelName;

    private String hotelAddress;

    @OneToOne
    private TripMember tripMember;

}
