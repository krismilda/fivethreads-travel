package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Room.FindAll", query = "SELECT  r FROM  Room as r"),
        @NamedQuery(name = "Room.ExistsByNumberAndApartmentId",
        query = "SELECT r FROM Room as r WHERE  r.apartment.id =: apartment_ID " +
                "AND r.number =: number"),
        @NamedQuery(name = "Room.FindAllUnoccupiedRooms", query = "SELECT r FROM Room AS r " +
                "WHERE r.id NOT IN " +
                "(SELECT ta.room FROM TripAccommodation AS ta WHERE ta.room IS NOT NULL " +
                "AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate)"),
        @NamedQuery(name = "Room.FindUnoccupiedRoomsByApartmentId", query =
                "SELECT r FROM Room as r WHERE r.apartment.id =: apartment_ID AND r.id NOT IN " +
                        "(SELECT ta.room from TripAccommodation as ta WHERE ta.room IS NOT NULL " +
                        "AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate)"),
        @NamedQuery(name = "Room.FindUnoccupiedRoomsByCity", query = "SELECT " +
                "r FROM Room as r WHERE  r.id NOT IN " +
                "(SELECT ta.room FROM TripAccommodation AS ta WHERE ta.room IS NOT NULL " +
                "                AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate " +
                "                AND ta.hotelAddress.city =: city )")

})
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

    @Version
    private Long version;

    public Room(){}
    public Room(Long number, Long capacity) {
        this.number = number;
        this.capacity = capacity;
    }
}
