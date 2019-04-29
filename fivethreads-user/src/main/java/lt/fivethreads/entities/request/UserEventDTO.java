package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEventDTO extends EventDTO {
    private String userEmail;
}
