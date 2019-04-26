package lt.fivethreads.mapper;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.AddressService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripMapper {

    @Autowired
    UserService userService;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    TripMemberMapper tripMemberMapper;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressMapper addressMapper;
    public Trip ConvertCreateTripFormToTrip(CreateTripForm form, String organizer_email) {
        User organizer = userService.getUserByEmail(organizer_email);
        Trip trip = new Trip();
        trip.setStartDate(form.getStartDate());
        trip.setFinishDate(form.getFinishDate());
        trip.setArrival(addressMapper.convertFullAddressToAddress(form.getArrival()));
        trip.setDeparture(addressMapper.convertFullAddressToAddress(form.getDeparture()));
        trip.setOrganizer(organizer);
        trip.setIsFlexible(form.getIsFlexible());
        trip.setIsCombined(false);
        for (TripMemberDTO tripMemberCreateDAO : form.getTripMembers()
        ) {
            TripMember tripMember = tripMemberMapper.convertTripMemberDTOtoTripMember(tripMemberCreateDAO);
            tripMember.setTrip(trip);
            tripMember.setTripAcceptance(TripAcceptance.PENDING);
            trip.getTripMembers().add(tripMember);
        }
        return trip;
    }

    public TripCancellation convertCancelledTripToObject(CancelledTrip cancelledTrip) {
        TripCancellation tripCancellation = new TripCancellation();
        TripMember tripMember = tripMemberRepository.getTripMemberByTripIDAndEmail(cancelledTrip.getTripID(), cancelledTrip.getEmail());
        tripCancellation.setTripMember(tripMember);
        tripCancellation.setReason(cancelledTrip.getReason());
        return tripCancellation;
    }

    public TripDTO converTripToTripDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        List<TripMemberDTO> tripMemberDTOList = new ArrayList<>();
        for (TripMember tripMember : trip.getTripMembers()
        ) {
            tripMemberDTOList.add(tripMemberMapper.convertTripMemberToTripMemberDTO(tripMember));
        }
        tripDTO.setTripMembers(tripMemberDTOList);
        tripDTO.setArrival(addressMapper.convertAddressToShortAddress(trip.getArrival()));
        tripDTO.setDeparture(addressMapper.convertAddressToShortAddress(trip.getDeparture()));
        tripDTO.setStartDate(trip.getStartDate());
        tripDTO.setFinishDate(trip.getFinishDate());
        tripDTO.setIsCombined(trip.getIsCombined());
        tripDTO.setIsFlexible(trip.getIsFlexible());
        tripDTO.setOrganizer_email(trip.getOrganizer().getEmail());
        return tripDTO;
    }

}
