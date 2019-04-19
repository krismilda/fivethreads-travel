package lt.fivethreads.entities.request;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.AccommodationType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class TripAccommodationForm {
    @DateTimeFormat
    @NotNull(message = "TripAccommodation Start cannot be null.")
    private Date accommodationStart;
    @DateTimeFormat
    @NotNull(message = "TripAccommodation Finish cannot be null.")
    private Date accommodationFinish;
    private long roomId;
    @NotNull(message = "AccommodationType cannot be null.")
    private AccommodationType accommodationType;
    @NotNull(message = "HotelName cannot be null.")
    private String hotelName;
    @NotNull(message = "HotelAddress cannot be null.")
    private String hotelAddress;
    private double price;
    @NotNull(message = "TripMember cannot be null")
    private long tripMemberId;
}
