package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomReservations {
    Long roomID;
    Date start;
    Date finish;
}
