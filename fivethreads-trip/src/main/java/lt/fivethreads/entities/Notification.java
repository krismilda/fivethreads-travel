package lt.fivethreads.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Notification.FindAllByEmail", query = "select tr from Notification as tr " +
                "JOIN FETCH tr.user as m " +
                "WHERE m.email=:email")
})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    private Boolean isActive;

    private String name;

    private Date created_date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "TRIP_HISTORY_ID")
    private TripHistory tripHistory;
}
