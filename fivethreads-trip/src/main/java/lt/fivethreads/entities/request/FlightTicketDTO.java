package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlightTicketDTO {

    private double price;

    private List<FileDTO> files = new ArrayList<>();
}