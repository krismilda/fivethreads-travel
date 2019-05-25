package lt.fivethreads.controller;

import lt.fivethreads.entities.rest.*;
import lt.fivethreads.services.StatisticServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StatisticController {


    @Autowired
    StatisticServiceImplementation statisticServiceImplementation;

    @GetMapping("/countTripsDate/start={start}&finish={finish}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripCount> tripCount(@PathVariable("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start, @PathVariable("finish") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date finish) {
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
        return statisticServiceImplementation.countTripList(start,finish, role, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/countTripsByUser/userIDs={userIDs}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public List<UserTripCountDTO> tripCountByUser(@PathVariable("userIDs") Long [] usersID ) {
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

    @GetMapping("/TripsByOffice/officeIDs={officeIDs}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN') or hasRole('USER') ")
    public List<TripCountByOfficeDTO> tripsByOffice(@PathVariable("officeIDs") Long [] officeIDs) {
        return statisticServiceImplementation.getTripCountByOffice(this.getRole(SecurityContextHolder.getContext().getAuthentication().getAuthorities()), SecurityContextHolder.getContext().getAuthentication().getName(), officeIDs);
    }

    public String getRole(Collection authorities){
        String role=null;
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_USER"))) {
            role="ROLE_USER";
        }
        if (authorities.stream().anyMatch(r -> r.toString().equals("ROLE_ADMIN"))) {
            role="ROLE_ADMIN";
        }
        return role;
    }
}
