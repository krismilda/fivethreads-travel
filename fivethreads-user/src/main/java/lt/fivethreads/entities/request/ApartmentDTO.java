package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDTO {
    private Long id;
    private String address;
    private Long officeId;
}
