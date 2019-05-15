package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class DateRangeDTO {
    @NotNull(message="Start date cannot be null.")
    Date start;
    @NotNull(message="Finish date cannot be null.")
    Date finish;
}