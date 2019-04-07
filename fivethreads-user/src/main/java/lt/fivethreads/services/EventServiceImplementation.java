package lt.fivethreads.services;

import lt.fivethreads.entities.Event;
import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.mapper.EventMapper;
import lt.fivethreads.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceImplementation implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Override
    public void createEvents(List<EventDTO> events) {
        List<Event> eventEntities = new ArrayList<>();
        for (EventDTO event: events) {
            Event eventEntity = eventMapper.getEvent(event);
            eventEntities.add(eventEntity);
        }

        eventRepository.saveAll(eventEntities);
    }
}
