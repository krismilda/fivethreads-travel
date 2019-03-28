package lt.fivethreads.services;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;
import lt.fivethreads.mapper.RoomMapper;
import lt.fivethreads.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomServiceImplementation implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomMapper roomMapper;


    @Override
    public List<RoomDTO> getAllRooms() {

        List<Room> rooms= roomRepository.findAll();
        return rooms.stream()
                .map(e -> roomMapper.getRoomDTO(e))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong roomId"));
        return roomMapper.getRoomDTO(room);
    }

    @Override
    public void updateRoom (RoomDTO roomDTO) {

        Room room = roomRepository.findById(roomDTO.getId())
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Wrong roomId"));
        room.setNumber(roomDTO.getNumber());
        room.setCapacity(roomDTO.getCapacity());
        Apartment apartment= new Apartment();
        apartment.setId(roomDTO.getApartmentId());
        room.setApartment(apartment);
        roomRepository.save(room);
    }


    public void deleteRoom(Long id) {
        roomRepository.deleteById(id); }


    public void createRoom (RoomForm roomForm) {
        Room room_to_save = roomMapper.convertRegisteredRoomToRoom(roomForm);
        roomRepository.save(room_to_save);
    }

    @Override
    public boolean checkIfRoomExists(Long number, Long apartmentId) {
        return roomRepository.existsByNumberAndApartmentId(number, apartmentId);
    }
}
