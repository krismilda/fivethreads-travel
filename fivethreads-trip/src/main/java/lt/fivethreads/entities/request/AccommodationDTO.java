package lt.fivethreads.entities.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.AccommodationType;
import lt.fivethreads.entities.File;
import lt.fivethreads.entities.TripMember;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AccommodationDTO {

    @DateTimeFormat
    @NotNull(message = "TripAccommodation Start cannot be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date accommodationStart;

    @DateTimeFormat
    @NotNull(message = "TripAccommodation Finish cannot be null.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date accommodationFinish;

    private double price;

    private AccommodationType accommodationType;

    private List<FileDTO> files = new ArrayList<>();
}

