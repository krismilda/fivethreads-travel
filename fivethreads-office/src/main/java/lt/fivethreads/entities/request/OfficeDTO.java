package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficeDTO {
    private Long id;
    private String name;
    private FullAddressDTO address;
    private Long version;
}
