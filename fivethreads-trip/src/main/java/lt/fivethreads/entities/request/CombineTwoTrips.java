package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CombineTwoTrips {
    @NotNull(message = "Trip ID cannot be null.")
    private Long tripID1;
    @NotNull(message = "Trip ID cannot be null.")
    private Long tripID2;
    @NotNull(message="Start date cannot be null.")
    @DateTimeFormat
    private Date startDate;
    @DateTimeFormat
    @NotNull(message="Finish date cannot be null.")
    private Date finishDate;
    @NotNull(message="Arrival cannot be null.")
    private FullAddressDTO arrival;
    @NotNull(message="Departure cannot be null.")
    private FullAddressDTO departure;
}
