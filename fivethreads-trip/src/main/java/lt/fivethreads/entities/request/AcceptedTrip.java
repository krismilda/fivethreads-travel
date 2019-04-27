package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AcceptedTrip {

    @NotNull
    private long tripID;

    @NotNull
    private Boolean isFlightTickedNeeded;

    @NotNull
    private Boolean isAccommodationNeeded;

    @NotNull
    private Boolean isCarNeeded;

    private FlightTicketDTO flightTicketDTO;

    private CarTicketDTO carTicketDTO;

    private AccommodationDTO accommodationDTO;
}
