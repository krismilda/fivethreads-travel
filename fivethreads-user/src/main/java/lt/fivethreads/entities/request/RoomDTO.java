package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {
    private Long id;
    private Long number;
    private Long capacity;
    private Long apartmentId;
}
