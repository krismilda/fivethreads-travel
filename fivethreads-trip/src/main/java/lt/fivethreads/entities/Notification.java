package lt.fivethreads.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Boolean isActive;

    private String name;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
