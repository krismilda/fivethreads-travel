package lt.fivethreads.entities.request;


import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.AccommodationType;
import lt.fivethreads.entities.File;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.TripMember;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TripAccommodationDTO {
    @NotNull(message = "ID cannot be null.")
    private Long id;
    @DateTimeFormat
    @NotNull(message = "TripAccommodation Start cannot be null.")
    private Date accommodationStart;
    @DateTimeFormat
    @NotNull(message = "TripAccommodation Finish cannot be null.")
    private Date accommodationFinish;
    private Long roomId;
    @NotNull(message = "AccommodationType cannot be null.")
    private AccommodationType accommodationType;
    private String hotelName;
    private String hotelAddress;
    private Double price;
    @NotNull(message = "TripMember cannot be null")
    private Long tripMemberId;

}
