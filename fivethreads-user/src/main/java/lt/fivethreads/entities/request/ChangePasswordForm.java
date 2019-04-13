package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordForm {
    @NotNull(message="Email cannot be null.")
    @Email(message="Wrong email format.")
    private String email;
    @NotNull (message = "Password cannot be null.")
    private String password;
}