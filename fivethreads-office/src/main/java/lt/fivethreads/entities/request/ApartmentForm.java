package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.Address;

@Getter
@Setter
public class ApartmentForm {
    private FullAddressDTO address;
    private Long officeId;
}
