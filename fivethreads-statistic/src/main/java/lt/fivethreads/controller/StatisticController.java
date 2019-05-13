package lt.fivethreads.controller;

import lt.fivethreads.entities.rest.DateRangeDTO;
import lt.fivethreads.entities.rest.TripCount;
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

    @GetMapping("/countTrips/")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripCount> tripCount(@Validated @RequestBody DateRangeDTO dateRangeDTO) {
        String role=null;
        Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))) {
            role="ROLE_USER";
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ORGANIZER"))) {
            role="ROLE_ORGANIZER";
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"))) {
            role="ROLE_ADMIN";
        }
        return statisticServiceImplementation.countTripList(dateRangeDTO, role, SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
