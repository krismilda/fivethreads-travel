package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRIP_MEMBER_HISTORY")
@Getter
@Setter
public class TripMemberHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message="Email cannot be null.")
    @Email(message="Wrong email format.")
    @Column(name = "email")
    private String email;

    @NotNull(message = "FirstName cannot be null.")
    @Column(name = "firstName")
    private String firstname;

    @NotNull(message = "LastName cannot be null.")
    @Column(name = "lastName")
    private String lastName;

    @NotNull(message = "Phone cannot be null.")
    private String phone;

    @NotNull(message="Organizer cannot be null.")
    @ManyToOne
    @JoinColumn(name="trip_history_id")
    private TripHistory tripHistory;
}
