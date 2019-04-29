package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedUserDTO extends UserDTO {
    private Long officeId;
    private String officeName;
}
