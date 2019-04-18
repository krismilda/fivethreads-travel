package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class OccupancyDTO {
    @NotNull(message = "ID cannot be null")
    private Long id;
    @NotNull(message = "Room cannot be null")
    private Long roomId;
    @NotNull(message = "User cannot be null")
    private Long userId;
    @NotNull(message = "Trip cannot be null")
    private Long tripId;
    @NotNull(message="Start date cannot be null.")
    @DateTimeFormat
    private Date startDate;
    @DateTimeFormat
    @NotNull(message="Finish date cannot be null.")
    private Date finishDate;
}
