package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.request.FullAddressDTO;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CsvTrip {
    private Long id;
    private Date startDate;
    private Date finishDate;
    private Boolean isCombined;
    private Boolean isFlexible;
    private TripStatus tripStatus;
    private String departureCountry;
    private String departureCity;
    private String departureStreet;
    private String departureHouseNumber;
    private double departureLatitude;
    private double departureLongitude;
    private String arrivalCountry;
    private String arrivalCity;
    private String arrivalStreet;
    private String arrivalHouseNumber;
    private double arrivalLatitude;
    private double arrivalLongitude;
    private String organizer;
    private List<String> tripMemberList;

}
