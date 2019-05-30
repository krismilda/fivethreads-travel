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
                "WHERE m.email=:email AND tr.notificationType in ('ForApproval', 'InformationChanged', 'Deleted')"),
        @NamedQuery(name = "Notification.FindAllOrganizerByEmail", query = "select tr from Notification as tr " +
                "JOIN FETCH tr.tripHistory as h " +
                "JOIN FETCH tr.user as m " +
                "JOIN FETCH h.organizer as u " +
                "WHERE (u.email=:email AND tr.notificationType in ('Approved', 'Cancelled')) " +
                "OR m.email=:email AND tr.notificationType in ('ForApproval', 'InformationChanged', 'Deleted')"),
        @NamedQuery(name = "Notification.CountUser", query = "select count(tr.id) from Notification as tr " +
                "JOIN tr.user as m " +
                "WHERE m.email=:email AND tr.notificationType in ('ForApproval', 'InformationChanged', 'Deleted')"),
        @NamedQuery(name = "Notification.CountOrganizer", query = "select count(tr.id) from Notification as tr " +
                "JOIN tr.tripHistory as h " +
                "JOIN tr.user as m " +
                "JOIN h.organizer as u " +
                "WHERE (u.email=:email AND tr.notificationType in ('Approved', 'Cancelled')) " +
                "OR m.email=:email AND tr.notificationType in ('ForApproval', 'InformationChanged', 'Deleted')"),
        @NamedQuery(name = "Notification.FindByID", query = "select tr from Notification as tr " +
                "WHERE tr.id=:id")
})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Boolean isActive;

    private Boolean isAnswered;

    private String name;

    private Date created_date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "TRIP_HISTORY_ID")
    private TripHistory tripHistory;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String reason;
}
