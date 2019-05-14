package lt.fivethreads.controller;

import lt.fivethreads.entities.rest.*;
import lt.fivethreads.services.StatisticServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StatisticController {


    @Autowired
    StatisticServiceImplementation statisticServiceImplementation;

    @GetMapping("/countTripsDate/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripCount> tripCount(@Validated @RequestBody DateRangeDTO dateRangeDTO) {
        return statisticServiceImplementation.countTripList(dateRangeDTO, this.getRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities()), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/countTripsByUser/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public List<UserTripCountDTO> tripCountByUser(@Validated @RequestBody IDList usersID) {
        return statisticServiceImplementation.countTripByUser(usersID);
    }

    @GetMapping("/TripsByPrice/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripsByPrice> tripsByPrice() {
        return statisticServiceImplementation.getTripsByPrice(this.getRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities()), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/TripsByDuration")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripByDuration> tripsByDuration() {
        return statisticServiceImplementation.getTripByDuration(this.getRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities()), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public String getRole(Collection authorities){
        String role=null;
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))) {
            role="ROLE_USER";
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))) {
            role="ROLE_ORGANIZER";
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"))) {
            role="ROLE_ADMIN";
        }
        return role;
    }
}
