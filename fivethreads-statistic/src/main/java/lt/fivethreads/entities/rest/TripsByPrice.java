package lt.fivethreads.entities.rest;

import lombok.Getter;
import lombok.Setter;
import lt.fivethreads.entities.request.TripDTO;


@Getter
@Setter
public class TripsByPrice {
    private TripDTO tripInfo;
    private Double totalPrice;
}
