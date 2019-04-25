package lt.fivethreads.mapper;

import lt.fivethreads.entities.*;
import lt.fivethreads.entities.request.AccommodationDTO;
import lt.fivethreads.entities.request.CarTicketDTO;
import lt.fivethreads.entities.request.FlightTicketDTO;
import lt.fivethreads.entities.request.TripMemberDTO;
import lt.fivethreads.repositories.TripMemberRepository;
import lt.fivethreads.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripMemberMapper {
    @Autowired
    UserService userService;

    @Autowired
    TripMemberRepository tripMemberRepository;

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
                carTicketDTO.getFileID().add(file.getId());
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
                accommodationDTO.getFileID().add(file.getId());
            }
        }
        return accommodationDTO;
    }

    public FlightTicketDTO convertFlightTicketToFlightTicketDAO(FlightTicket flightTicket) {
        FlightTicketDTO flightTicketDTO = new FlightTicketDTO();
        if(flightTicket.getFile()!=null){
            for (File file : flightTicket.getFile()
            ) {
                flightTicketDTO.getFileID().add(file.getId());
            }
        }
        return flightTicketDTO;
    }
}
