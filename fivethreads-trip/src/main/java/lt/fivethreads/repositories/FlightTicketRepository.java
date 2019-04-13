package lt.fivethreads.repositories;

import lt.fivethreads.entities.FlightTicket;

public interface FlightTicketRepository {
    void saveFlightTicket(FlightTicket flightTicket);
    void updateFlightTicket(FlightTicket flightTicket);
}
