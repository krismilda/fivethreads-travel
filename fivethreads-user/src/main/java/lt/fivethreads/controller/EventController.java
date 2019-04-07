package lt.fivethreads.controller;

import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class EventController {
    @Autowired
    EventService eventService;

    @GetMapping("/user/{userId}/events")
    public ResponseEntity<?> getUserEvents(@PathVariable("userId") long userId) {
        List<EventDTO> events = eventService.getUserEvents(userId);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
