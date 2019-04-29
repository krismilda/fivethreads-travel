package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
public class AccommodationDTOWithoutFiles {
    @DateTimeFormat
    @NotNull(message = "TripAccommodation Start cannot be null.")
    private Date accommodationStart;

    @DateTimeFormat
    @NotNull(message = "TripAccommodation Finish cannot be null.")
    private Date accommodationFinish;
}
