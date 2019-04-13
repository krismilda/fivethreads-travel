package lt.fivethreads.repositories;

import lt.fivethreads.entities.CarTicket;

public interface CarTicketRepository {
    void saveCarTicket(CarTicket carTicket);
    void updateCarTicket(CarTicket carTicket);
}
