package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FLIGHT_TICKET")
@Getter
@Setter
public class FlightTicket implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="unique_id")
    private String uniqueID;

    private double price;

    @OneToOne(mappedBy = "flightTicket")
    private TripMember tripMember;

    @OneToMany
    @JoinColumns({
            @JoinColumn(
                    name = "tickect_id",
                    referencedColumnName = "unique_id")
    })
    private List<File> file = new ArrayList<>();

}
