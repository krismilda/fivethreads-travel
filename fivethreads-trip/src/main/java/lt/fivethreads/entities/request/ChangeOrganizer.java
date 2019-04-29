package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangeOrganizer {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull(message="Organizer cannot be null.")
    private String organizer_email;
}
