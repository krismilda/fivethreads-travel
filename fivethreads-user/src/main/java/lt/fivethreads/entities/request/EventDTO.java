package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
}
