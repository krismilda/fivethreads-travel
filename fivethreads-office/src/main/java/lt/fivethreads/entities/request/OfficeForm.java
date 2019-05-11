package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficeForm {
    private String name;
    private FullAddressDTO address;
}
