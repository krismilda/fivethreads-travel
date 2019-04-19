package lt.fivethreads.mapper;

import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.TripAccommodation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.request.TripAccommodationDTO;
import lt.fivethreads.entities.request.TripAccommodationForm;
import lt.fivethreads.repositories.RoomRepository;
import lt.fivethreads.repositories.TripMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripAccommodationMapper {


    @Autowired
    TripMemberRepository tripMemberRepository;

    @Autowired
    RoomRepository roomRepository;

    public TripAccommodation convertCreateTripAccommodationFormToTripAccommodation(TripAccommodationForm form){

        TripAccommodation tripAccommodation = new TripAccommodation();

        switch (form.getAccommodationType()){
            case HOTEL:
                tripAccommodation.setHotelAddress(form.getHotelAddress());
                tripAccommodation.setHotelName(form.getHotelName());
                tripAccommodation.setPrice(form.getPrice());
                break;


            case DEVRIDGE_APARTAMENTS:
                Room room = roomRepository.getOne(form.getRoomId());
                tripAccommodation.setRoom(room);
                tripAccommodation.setHotelName(room.getApartment().getOffice().getName());
                tripAccommodation.setHotelAddress(room.getApartment().getOffice().getAddress()+
                        ", "+ room.getApartment().getAddress());
                break;
               
        }
        TripMember member = tripMemberRepository.findById(form.getTripMemberId());
        tripAccommodation.setAccommodationStart(form.getAccommodationStart());
        tripAccommodation.setAccommodationFinish(form.getAccommodationFinish());
        tripAccommodation.setAccommodationType(form.getAccommodationType());
        tripAccommodation.setTripMember(member);

        return tripAccommodation;
    }

    public TripAccommodationDTO getTripAccommodationDTO(TripAccommodation tripAccommodation){
        TripAccommodationDTO tripAccommodationDTO = new TripAccommodationDTO();

        tripAccommodationDTO.setId(tripAccommodation.getId());
        tripAccommodationDTO.setAccommodationStart(tripAccommodation.getAccommodationStart());
        tripAccommodationDTO.setAccommodationFinish(tripAccommodation.getAccommodationFinish());
        tripAccommodationDTO.setTripMemberId(tripAccommodation.getTripMember().getId());
        tripAccommodationDTO.setAccommodationType(tripAccommodation.getAccommodationType());
        if(tripAccommodation.getRoom() != null) tripAccommodationDTO.setRoomId(tripAccommodation.getRoom().getId());
        tripAccommodationDTO.setHotelAddress(tripAccommodation.getHotelAddress());
        tripAccommodationDTO.setHotelName(tripAccommodation.getHotelName());
        tripAccommodationDTO.setPrice(tripAccommodation.getPrice());

        return tripAccommodationDTO;
    }
}
