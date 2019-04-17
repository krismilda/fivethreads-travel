package lt.fivethreads.entities;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Long number;

    @Column(name ="capacity")
    private Long capacity;

    @JoinColumn(name = "apartmentId")
    @ManyToOne(targetEntity = Apartment.class, fetch = FetchType.LAZY)
    @NotNull
    private Apartment apartment;

    public Room(){}
    public Room(Long number, Long capacity) {
        this.number = number;
        this.capacity = capacity;
    }
}
