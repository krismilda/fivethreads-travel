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
        @NamedQuery(name = "Room.ExistsByNameAndApartmentId",
                query = "SELECT r FROM Room as r WHERE  r.apartment.id =: apartment_ID " +
                        "AND r.name =: name"),
        @NamedQuery(name = "Room.FindAllUnoccupiedRooms", query = "SELECT r FROM Room AS r " +
                "WHERE r.id NOT IN " +
                "(SELECT ta.room FROM TripAccommodation AS ta WHERE ta.room IS NOT NULL " +
                "AND ((ta.accommodationStart <=:startDate  AND ta.accommodationFinish >=:startDate) or " +
                "(ta.accommodationStart <=:finishDate  AND ta.accommodationFinish >=:finishDate)))"),
        @NamedQuery(name = "Room.FindUnoccupiedRoomsByApartmentId", query =
                "SELECT r FROM Room as r WHERE r.apartment.id =: apartment_ID AND r.id NOT IN " +
                        "(SELECT ta.room from TripAccommodation as ta WHERE ta.room IS NOT NULL " +
                        "AND ((ta.accommodationStart <=:startDate  AND ta.accommodationFinish >=:startDate) or " +
                        "(ta.accommodationStart <=:finishDate  AND ta.accommodationFinish >=:finishDate)))"),
        @NamedQuery(name = "Room.FindUnoccupiedRoomsByCity", query = "SELECT " +
                "r FROM Room as r, Apartment as a WHERE a.address.city =:city AND r.apartment.id = a.id AND r.id NOT IN " +
                "(SELECT ta.room FROM TripAccommodation AS ta WHERE ta.room IS NOT NULL  " +
                "AND ((ta.accommodationStart <=:startDate  AND ta.accommodationFinish >=:startDate) or " +
                "(ta.accommodationStart <=:finishDate  AND ta.accommodationFinish >=:finishDate)))"
        )
       /* @NamedQuery(name = "Room.FindLastDefaultName", query = " SELECT " +
                " r.name FROM Room as r WHERE r.apartment.id =: apartment_ID AND r.name LIKE :name order by " +
                " (SUBSTRING(r.Name, 13)) AS INT")*/
})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "capacity")
    private Long capacity;

    @JoinColumn(name = "apartmentId")
    @ManyToOne(targetEntity = Apartment.class, fetch = FetchType.LAZY)
    @NotNull
    private Apartment apartment;

    @Column(name = "name")
    @NotNull
    private String name;

    @Version
    private Long version;


    public Room() {
    }

    public Room(String name, Long capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
