package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TripDTO {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @NotNull(message="Start date cannot be null.")
    @DateTimeFormat
    private Date startDate;
    @DateTimeFormat
    @NotNull(message="Finish date cannot be null.")
    private Date finishDate;
    @NotNull(message="Arrival cannot be null.")
    private String arrival;
    @NotNull(message="Departure cannot be null.")
    private String departure;
    private List<TripMemberDTO> tripMembers;
    @NotNull(message="Organizer cannot be null.")
    private String organizer_email;
}
