package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateTripForm {
    private Date startDate;
    private Date finishDate;
    private String arrival;
    private String departure;
    private List<Long> tripMembersID;
}
