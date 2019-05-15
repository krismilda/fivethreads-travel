package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.request.ExtendedUserDTO;

@Getter
@Setter
public class UserTripCountDTO {
    private ExtendedUserDTO user;
    int count;
}
