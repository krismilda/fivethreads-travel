package lt.fivethreads.entities.rest;

import lt.fivethreads.entities.request.ShortAddressDTO;
import lt.fivethreads.entities.request.TripMemberDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class TripInfo {
    private Long id;
    @NotNull(message="Start date cannot be null.")
    private Date startDate;
    @NotNull(message="Finish date cannot be null.")
    private Date finishDate;
    @NotNull(message="Arrival cannot be null.")
    private ShortAddressDTO arrival;
    @NotNull(message="Departure cannot be null.")
    private ShortAddressDTO departure;
    private List<TripMemberDTO> tripMembers;
    @NotNull(message="Organizer cannot be null.")
    private String organizer_email;
    @NotNull(message = "Is flexible cannot be null.")
    private Boolean isFlexible;
    @NotNull(message = "IsCombined cannot be null")
    private Boolean isCombined;
    private String tripStatus;
}
