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
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL)
    private Set<Apartment> apartments;

    public Office (){}
    public Office(String name, String address, Apartment... apartments) {
        this.name = name;
        this.address = address;
        this.apartments = Stream.of(apartments).collect(Collectors.toSet());
        this.apartments.forEach(x -> x.setOffice(this));
    }
}
