package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ShortAddressDTO {
    @NotNull(message = "Full address cannot be null.")
    private String fullAddress;

    @NotNull(message = "Longitude number cannot be null.")
    private double longitude;

    @NotNull(message = "Latitude number cannot be null.")
    private double latitude;
}
