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
        @NamedQuery(name = "Notification.FindAllUserByEmail", query = "select tr from Notification as tr " +
                "JOIN FETCH tr.user as m " +
                "WHERE m.email=:email AND tr.notificationType in (0, 3)"),
        @NamedQuery(name = "Notification.FindAllOrganizerByEmail", query = "select tr from Notification as tr " +
                "JOIN FETCH tr.tripHistory as m " +
                "JOIN FETCH m.organizer as u " +
                "WHERE u.email=:email AND tr.notificationType in (1, 2)"),
        @NamedQuery(name = "Notification.FindByID", query = "select tr from Notification as tr " +
                "WHERE tr.id=:id")

})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Boolean isActive;

    private String name;

    private Date created_date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "TRIP_HISTORY_ID")
    private TripHistory tripHistory;

    private NotificationType notificationType;

    private String reason;
}
