package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private Set<String> role;
    private Long officeId;
}
