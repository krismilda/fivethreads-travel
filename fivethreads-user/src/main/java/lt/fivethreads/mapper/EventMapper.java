package lt.fivethreads.mapper;

import lt.fivethreads.entities.Event;
import lt.fivethreads.entities.User;
import lt.fivethreads.entities.request.EventDTO;
import lt.fivethreads.exception.user.UserNotFoundException;
import lt.fivethreads.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Component
public class EventMapper {
    @Autowired
    UserRepository userRepository;

    private HashMap<String, User> usersByEmail = new HashMap<>();

    public Event getEvent(EventDTO eventDTO) {

        loadUsers(true);
        Event event = new Event();
        event.setEndDate(eventDTO.getEndDate());
        event.setStartDate(eventDTO.getStartDate());

        if (!usersByEmail.containsKey(eventDTO.getUserEmail())) {
            loadUsers(false);
            if (!usersByEmail.containsKey(eventDTO.getUserEmail())) {
                throw new UserNotFoundException();
            }
        }

        event.setUser(usersByEmail.get(eventDTO.getUserEmail()));

        return event;
    }

    private void loadUsers(boolean lazyLoad) {
        try {
            if (!lazyLoad || usersByEmail.isEmpty()) {
                List<User> users = userRepository.findAll();
                for (User user : users) {
                    usersByEmail.put(user.getEmail(), user);
                }
            }
        } catch (Exception e) {
            System.out.println("Aaa");
        }
    }
}
