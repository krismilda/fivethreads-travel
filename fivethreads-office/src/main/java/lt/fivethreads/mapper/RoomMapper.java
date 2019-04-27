package lt.fivethreads.mapper;

import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;
import lt.fivethreads.repositories.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    @Autowired
    ApartmentRepository apartmentRepository;
    public Room convertRegisteredRoomToRoom(RoomForm roomForm){
        Room room = new Room();
        Apartment apartment;

        apartment = apartmentRepository.findById(roomForm.getApartmentId());
        room.setNumber(roomForm.getNumber());
        room.setCapacity(roomForm.getCapacity());
        room.setApartment(apartment);
        return room;
    }

    public RoomDTO getRoomDTO (Room room){
        RoomDTO roomDTO= new RoomDTO();

        roomDTO.setNumber(room.getNumber());
        roomDTO.setId(room.getId());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setApartmentId(room.getApartment().getId());

        return roomDTO;
    }
}
