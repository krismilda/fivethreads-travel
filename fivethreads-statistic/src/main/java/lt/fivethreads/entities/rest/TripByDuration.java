package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.request.TripDTO;

@Getter
@Setter
public class TripByDuration {
    private TripDTO tripInfo;
    private long duration_days;
}
