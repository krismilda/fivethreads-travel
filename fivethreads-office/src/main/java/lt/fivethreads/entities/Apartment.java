package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Apartment.FindAll", query = "SELECT  a FROM  Apartment as a"),
        @NamedQuery(name = "Apartment.ExistsByAddressAndOfficeId",
                query = "SELECT a FROM Apartment as a WHERE  a.address.latitude =: latitude AND a.address.longitude =: longitude " +
                        "AND a.office.id =: office_ID"),
        @NamedQuery(name = "Apartment.FindUnoccupiedAccommodationApartments", query = "" +
                "SELECT a FROM Apartment as a WHERE a.id NOT IN ( " +
                "SELECT r.apartment FROM Room as r JOIN TripAccommodation as ta ON r.id = ta.room " +
                "WHERE ta.room IS NOT NULL " +
                "AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate)"),
        @NamedQuery(name = "Apartment.FindUnoccupiedApartmentsByOfficeId",
                query = "SELECT a FROM Apartment as a WHERE a.office.id =: office_ID AND a.id NOT IN ( " +
                        "SELECT r.apartment FROM Room as r JOIN TripAccommodation as ta ON r.id = ta.room " +
                        "WHERE ta.room IS NOT NULL " +
                        "AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate)")
})
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="address")
    private Address address;

    @JoinColumn(name = "officeId")
    @ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)

    @NotNull
    private Office office;

    @OneToMany(mappedBy = "apartment")
    private Set<Room> rooms;

    public Apartment(){}
    public Apartment(Address address, Room... rooms) {
        this.address = address;
        this.rooms = Stream.of(rooms).collect(Collectors.toSet());
        this.rooms.forEach(x -> x.setApartment(this));
    }
}
