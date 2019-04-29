package lt.fivethreads.services;

import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.entities.request.UserEventDTO;

import java.util.List;

public interface EventService {

    void createEvents(List<UserEventDTO> users);

    List<EventDTO> getUserEvents(long userId);
}
