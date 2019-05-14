package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.request.OfficeDTO;

@Getter
@Setter
public class TripCountByOfficeDTO {
    private OfficeDTO office;
    private long count;
}
