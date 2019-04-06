package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.TripMember;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AcceptedTrip {

    @NotNull
    private long tripID;

    @NotNull
    private TripMemberDAO tripMemberDAO;
}
