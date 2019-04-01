package lt.fivethreads.entities;

import com.sun.javafx.beans.IDProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TRIP_CANCELLATION")
@Getter
@Setter
public class TripCancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String reason;

    @OneToOne
    private TripMember tripMember;
}
