package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
public class CarTicketDTOWithoutFiles {
    @DateTimeFormat
    @NotNull(message = "Car Rent Start cannot be null.")
    private Date carRentStart;

    @DateTimeFormat
    @NotNull(message = "Car Rent Finish cannot be null.")
    private Date carRentFinish;
}
