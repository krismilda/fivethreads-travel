package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FLIGHT_TICKET")
@Getter
@Setter
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private double price;

    @OneToOne(mappedBy = "flightTicket")
    private TripMember tripMember;

    @OneToMany
    @JoinColumn(name="tickect_id")
    private List<File> file;

}
