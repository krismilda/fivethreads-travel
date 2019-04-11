package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NotificationTripMemberDTO {
    @NotNull(message = "Email cannot be null.")
    @Email
    private String email;
    @NotNull(message = "FirstName cannot be null.")
    private String firstName;
    @NotNull(message = "LastName cannot be null.")
    private String lastName;
    @NotNull(message = "Phone cannot be null.")
    private String phone;
}
