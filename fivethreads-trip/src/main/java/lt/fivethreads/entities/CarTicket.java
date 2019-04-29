package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CAR_TICKET")
@Getter
@Setter
public class CarTicket  implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="unique_id")
    private String uniqueID;

    @DateTimeFormat
    @NotNull
    private Date carRentStart;

    @DateTimeFormat
    @NotNull
    private Date carRentFinish;

    private Double price;

    @OneToOne(mappedBy = "carTicket")
    private TripMember tripMember;

    @OneToMany
    @JoinColumns({
            @JoinColumn(
                    name = "tickect_id",
                    referencedColumnName = "unique_id")
    })
    private List<File> file = new ArrayList<>();
}
