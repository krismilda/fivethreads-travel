package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    @NotNull(message="Email cannot be null.")
    @Email(message="Wrong email format.")
    private String email;
    @NotNull (message = "Password cannot be null.")
    @Size(min=6, message = "Password must be longer that 6 symbols.")
    private String password;
    @NotNull(message = "FirstName cannot be null.")
    private String firstname;
    @NotNull(message = "LastName cannot be null.")
    private String lastname;
    @NotNull(message="Phone cannot be null")
    private String phone;
    @NotNull(message="Role cannot be null")
    private Set<String> role;
}
