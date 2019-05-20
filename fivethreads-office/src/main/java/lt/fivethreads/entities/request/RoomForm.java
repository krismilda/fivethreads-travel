package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomForm {
    private Long capacity;
    private Long apartmentId;
    private String name;
}
