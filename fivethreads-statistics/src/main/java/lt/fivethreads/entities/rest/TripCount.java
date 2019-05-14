package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class TripCount {
    private Date date;
    private Number count;
}