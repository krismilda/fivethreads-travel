package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ADDRESS")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Country cannot be null.")
    private String country;

    @NotNull(message = "City cannot be null.")
    private String city;

    @NotNull(message = "Street cannot be null.")
    private String street;

    private String houseNumber;

    private String flatNumber;

    @NotNull(message = "Longitude number cannot be null.")
    private double longitude;

    @NotNull(message = "Latitude number cannot be null.")
    private double latitude;
}
