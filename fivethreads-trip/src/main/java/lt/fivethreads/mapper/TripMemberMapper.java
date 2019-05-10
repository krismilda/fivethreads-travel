package lt.fivethreads.mapper;

import lt.fivethreads.Mapper.AddressMapper;
import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.*;
import lt.fivethreads.entities.request.Notifications.UserInformationDTO;
import lt.fivethreads.exception.WrongTripData;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.AddressService;
import lt.fivethreads.services.FileService;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripMemberMapper {
    @Autowired
    UserService userService;

    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    FileService fileService;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressMapper addressMapper;

    public TripMemberDTO convertTripMemberToTripMemberDTO(TripMember tripMember) {
        TripMemberDTO tripMemberDTO = new TripMemberDTO();
        tripMemberDTO.setEmail(tripMember.getUser().getEmail());
        tripMemberDTO.setIsAccommodationNeeded(tripMember.getIsAccommodationNeeded());
        tripMemberDTO.setIsCarNeeded(tripMember.getIsCarNeeded());
        tripMemberDTO.setIsFlightTickedNeeded(tripMember.getIsFlightTickedNeeded());
        tripMemberDTO.setStatus(tripMember.getTripAcceptance());
        if (tripMember.getCarTicket() != null && tripMember.getIsCarNeeded() == true) {
            tripMemberDTO.setCarTicketDTO(convertCarTicketToCarTicketDTO(tripMember.getCarTicket()));
        }
        if (tripMember.getTripAccommodation() != null && tripMember.getIsAccommodationNeeded() == true) {
            tripMemberDTO.setAccommodationDTO(convertAccomodationToAccomodationDTO(tripMember.getTripAccommodation()));
        }
        if (tripMember.getFlightTicket() != null && tripMember.getIsFlightTickedNeeded() == true) {
            tripMemberDTO.setFlightTicketDTO(convertFlightTicketToFlightTicketDAO(tripMember.getFlightTicket()));
        }
        return tripMemberDTO;
    }

    public TripMember convertTripMemberDTOtoTripMember(TripMemberDTO tripMemberDTO) {
        User user = userService.getUserByEmail(tripMemberDTO.getEmail());
        TripMember tripMember = new TripMember();
        tripMember.setUser(user);
        tripMember.setIsAccommodationNeeded(tripMemberDTO.getIsAccommodationNeeded());
        if (tripMemberDTO.getIsAccommodationNeeded() && tripMemberDTO.getAccommodationDTO() != null) {
            TripAccommodation tripAccommodation = convertAccommodationDTOToTripAccommodation(tripMemberDTO.getAccommodationDTO());
            tripAccommodation.setTripMember(tripMember);
            tripMember.setTripAccommodation(tripAccommodation);
        }
        tripMember.setIsFlightTickedNeeded(tripMemberDTO.getIsFlightTickedNeeded());
        if (tripMemberDTO.getIsCarNeeded() && tripMemberDTO.getCarTicketDTO() != null) {
            CarTicket carTicket = convertCarTicketDtoToCarTicket(tripMemberDTO.getCarTicketDTO());
            carTicket.setTripMember(tripMember);
            tripMember.setCarTicket(carTicket);
        }
        tripMember.setIsCarNeeded(tripMemberDTO.getIsCarNeeded());
        return tripMember;
    }


    public CarTicket convertCarTicketDtoToCarTicket(CarTicketDTO carTicketDTO) {
        CarTicket carTicket = new CarTicket();
        carTicket.setCarRentStart(carTicketDTO.getCarRentStart());
        carTicket.setCarRentFinish(carTicketDTO.getCarRentFinish());
        return carTicket;
    }

    public TripAccommodation convertAccommodationDTOToTripAccommodation(AccommodationDTO accommodationDTO) {
        TripAccommodation tripAccommodation = new TripAccommodation();
        tripAccommodation.setAccommodationStart(accommodationDTO.getAccommodationStart());
        tripAccommodation.setAccommodationFinish(accommodationDTO.getAccommodationFinish());
        return tripAccommodation;
    }

    public CarTicketDTO convertCarTicketToCarTicketDTO(CarTicket carTicket) {
        CarTicketDTO carTicketDTO = new CarTicketDTO();
        carTicketDTO.setCarRentFinish(carTicket.getCarRentFinish());
        carTicketDTO.setCarRentStart(carTicket.getCarRentStart());
        if(carTicket.getFile()!=null){
            for (File file : carTicket.getFile()
            ) {
                carTicketDTO.getFiles().add(fileService.getFileById(file.getId()));
                carTicketDTO.setPrice(carTicket.getPrice());
            }
        }

        return carTicketDTO;
    }

    public AccommodationDTO convertAccomodationToAccomodationDTO(TripAccommodation tripAccommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setAccommodationStart(tripAccommodation.getAccommodationStart());
        accommodationDTO.setAccommodationFinish(tripAccommodation.getAccommodationFinish());
        if(tripAccommodation.getFile()!=null){
            for (File file : tripAccommodation.getFile()
            ) {
                accommodationDTO.getFiles().add(fileService.getFileById(file.getId()));
                accommodationDTO.setPrice(tripAccommodation.getPrice());
            }
        }
        return accommodationDTO;
    }

    public FlightTicketDTO convertFlightTicketToFlightTicketDAO(FlightTicket flightTicket) {
        FlightTicketDTO flightTicketDTO = new FlightTicketDTO();
        if(flightTicket.getFile()!=null){
            for (File file : flightTicket.getFile()
            ) {
                flightTicketDTO.getFiles().add(fileService.getFileById(file.getId()));
                flightTicketDTO.setPrice(flightTicket.getPrice());
            }
        }
        return flightTicketDTO;
    }

    public UserTripDTO convertTripToUserTripDTO(Trip trip, String email){
        UserTripDTO userTripDTO = new UserTripDTO();
        userTripDTO.setId(trip.getId());
        List<UserInformationDTO> otherTripMembers = getotherTripMembers(trip, email);
        userTripDTO.setOtherTripMembers(otherTripMembers);
        userTripDTO.setArrival(addressMapper.convertAddressToShortAddress(trip.getArrival()));
        userTripDTO.setDeparture(addressMapper.convertAddressToShortAddress(trip.getDeparture()));
        userTripDTO.setStartDate(trip.getStartDate());
        userTripDTO.setFinishDate(trip.getFinishDate());
        userTripDTO.setIsCombined(trip.getIsCombined());
        userTripDTO.setIsFlexible(trip.getIsFlexible());
        userTripDTO.setOrganizer_email(trip.getOrganizer().getEmail());
        TripMember tripMember = trip.getTripMembers()
                .stream()
                .filter(e -> e.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new WrongTripData("User does not exists."));
        TripMemberDTO tripMemberDTO = convertTripMemberToTripMemberDTO(tripMember);
        userTripDTO.setTripMemberDTO(tripMemberDTO);
        return userTripDTO;
    }

    public List<UserInformationDTO> getotherTripMembers(Trip trip, String email){
        List<UserInformationDTO> otherTripMembers = new ArrayList<>();
        for (TripMember tripMemberInTrip : trip.getTripMembers()
        ) {
            if(!tripMemberInTrip.getUser().getEmail().equals(email)){
                UserInformationDTO userInformationDTO1 = new UserInformationDTO();
                userInformationDTO1.setEmail(tripMemberInTrip.getUser().getEmail());
                userInformationDTO1.setFirstName(tripMemberInTrip.getUser().getFirstname());
                userInformationDTO1.setLastName(tripMemberInTrip.getUser().getLastName());
                userInformationDTO1.setPhone(tripMemberInTrip.getUser().getPhone());
                otherTripMembers.add(userInformationDTO1);
            }
        }
        return otherTripMembers;
    }
}
