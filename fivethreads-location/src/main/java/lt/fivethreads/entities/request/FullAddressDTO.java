package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FullAddressDTO {
    @NotNull(message = "Country cannot be null.")
    private String country;

    @NotNull(message = "City cannot be null.")
    private String city;

    @NotNull(message = "Street cannot be null.")
    private String street;

    @NotNull(message = "House number cannot be null.")
    private String houseNumber;

    private String flatNumber;

    @NotNull(message = "Longitude number cannot be null.")
    private double longitude;

    @NotNull(message = "Latitude number cannot be null.")
    private double latitude;
}
