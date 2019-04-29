package lt.fivethreads.services;

import lt.fivethreads.entities.Event;
import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.entities.request.UserEventDTO;
import lt.fivethreads.mapper.EventMapper;
import lt.fivethreads.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventServiceImplementation implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Override
    public void createEvents(List<UserEventDTO> events) {
        List<Event> eventEntities = new ArrayList<>();
        for (UserEventDTO event: events) {
            Event eventEntity = eventMapper.getEvent(event);
            eventEntities.add(eventEntity);
        }

        eventRepository.saveAll(eventEntities);
    }

    public List<EventDTO> getUserEvents(long userId) {
        List<Event> events = eventRepository.getUserEvents(userId);
        return events.stream()
                .map(e -> eventMapper.getEventDTO(e))
                .collect(Collectors.toList());
    }
}
