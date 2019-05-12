package lt.fivethreads.importing.csv.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvOffice {
    private String name;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private double latitude;
    private double longitude;
}
