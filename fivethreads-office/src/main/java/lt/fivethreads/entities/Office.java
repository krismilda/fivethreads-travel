package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Office.FindAll", query = "SELECT  o FROM  Office as o"),
        @NamedQuery(name = "Office.ExistsByAddressAndName",
                query = "SELECT o FROM Office as o WHERE  o.address.latitude =: latitude AND o.address.longitude =: longitude " +
                        "AND o.name =: name"),
        @NamedQuery(name = "Office.FindUnoccupiedAccommodationOffices",
                query = "SELECT DISTINCT (o) FROM Office as o JOIN Apartment as a ON o.id = a.office " +
                        "AND a.id NOT IN (SELECT r.apartment FROM Room as r JOIN TripAccommodation as ta ON r.id = ta.room " +
                        "WHERE ta.room IS NOT NULL " +
                        "AND ta.accommodationStart <=: startDate  AND ta.accommodationFinish >=: finishDate)")

})
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="name")
    private String name;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="address")
    private Address address;

    @OneToMany(mappedBy = "office")
    private Set<Apartment> apartments;

    public Office (){}
    public Office(String name, Address address, Apartment... apartments) {
        this.name = name;
        this.address = address;
        this.apartments = Stream.of(apartments).collect(Collectors.toSet());
        this.apartments.forEach(x -> x.setOffice(this));
    }
}
