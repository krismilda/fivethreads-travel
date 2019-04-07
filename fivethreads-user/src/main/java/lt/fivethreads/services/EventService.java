package lt.fivethreads.services;

import lt.fivethreads.entities.request.EventDTO;

import java.util.List;

public interface EventService {

    void createEvents(List<EventDTO> users);
}
