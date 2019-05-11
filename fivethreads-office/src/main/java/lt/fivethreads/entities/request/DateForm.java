package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class DateForm {
    @NotNull(message = "Start Date cannot be null.")
    private Date startDate;
    @NotNull(message = "Finish Date cannot be null.")
    private Date finishDate;
}
