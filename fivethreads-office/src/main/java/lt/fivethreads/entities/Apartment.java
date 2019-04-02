package lt.fivethreads.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @JoinColumn(name = "officeId")
    @ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    private Office office;

    @OneToMany(mappedBy = "apartment")
    private Set<Room> rooms;

    public Apartment(){}
    public Apartment(String address, Room... rooms) {
        this.address = address;
        this.rooms = Stream.of(rooms).collect(Collectors.toSet());
        this.rooms.forEach(x -> x.setApartment(this));
    }
}
