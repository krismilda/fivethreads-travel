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
        room.setCapacity(roomForm.getCapacity());
        room.setApartment(apartment);
        room.setName(roomForm.getName());
        return room;
    }

    public RoomDTO getRoomDTO (Room room){
        RoomDTO roomDTO= new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setApartmentId(room.getApartment().getId());
        roomDTO.setName(room.getName());
        roomDTO.setVersion(room.getVersion());
        return roomDTO;
    }
}
