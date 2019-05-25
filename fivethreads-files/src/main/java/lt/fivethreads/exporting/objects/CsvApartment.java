package lt.fivethreads.exporting.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvApartment {
    private Long id;
    private Long officeId;
    private String officeName;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private double latitude;
    private double longitude;
}
