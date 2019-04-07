package lt.fivethreads.entities.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
    private String userEmail;
}
