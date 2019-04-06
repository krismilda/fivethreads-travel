package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CAR_TICKET")
@Getter
@Setter
public class CarTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DateTimeFormat
    @NotNull
    private Date carRentStart;

    @DateTimeFormat
    @NotNull
    private Date carRentFinish;

    private Double price;

    @OneToOne(mappedBy = "flightTicket")
    private TripMember tripMember;

    @OneToMany
    @JoinColumn(name="tickect_id")
    private List<File> file;
}
