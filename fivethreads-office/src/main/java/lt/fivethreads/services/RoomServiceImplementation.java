package lt.fivethreads.services;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;
import lt.fivethreads.mapper.RoomMapper;
import lt.fivethreads.repositories.RoomRepository;
import lt.fivethreads.validation.DateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomServiceImplementation implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    DateValidation dateValidation;

    public List<RoomDTO> getAllRooms() {

        List<Room> rooms= roomRepository.getAll();
        return rooms.stream()
                .map(e -> roomMapper.getRoomDTO(e))
                .collect(Collectors.toList());
    }

    public Room getRoomById(Long id) {
        Room room = roomRepository.findById(id);
        return room;
    }

    public Room updateRoom (RoomDTO roomDTO) {

        Room room = roomRepository.findById(roomDTO.getId());
        room.setNumber(roomDTO.getNumber());
        room.setCapacity(roomDTO.getCapacity());
        Apartment apartment= new Apartment();
        apartment.setId(roomDTO.getApartmentId());
        room.setApartment(apartment);
        return roomRepository.updateRoom(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteRoom(id); }

    public Room createRoom (RoomForm roomForm) {
        Room room_to_save = roomMapper.convertRegisteredRoomToRoom(roomForm);
        return roomRepository.createRoom(room_to_save);
    }

    public boolean checkIfRoomExists(Long number, Long apartmentId) {
        return roomRepository.existsByNumberAndId(number, apartmentId);
    }

    public List<RoomDTO> getAllUnoccupiedAccommodations(Date startDate, Date finishDate) {
        dateValidation.checkFinishStartDates(startDate,
                finishDate, "Finish date is earlier than start date.");
        dateValidation.checkStartDateToday(startDate);
        List<Room> roomList = roomRepository.getUnoccupiedRooms(startDate, finishDate);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for(Room room : roomList){
            roomDTOList.add(roomMapper.getRoomDTO(room));
        }
        return roomDTOList;
    }

    public List<RoomDTO> getAllUnoccupiedAccommodationsByApartmentId(Date startDate, Date finishDate, Long apartmentId) {
        dateValidation.checkFinishStartDates(startDate,
                finishDate, "Finish date is earlier than start date.");
        dateValidation.checkStartDateToday(startDate);
        List<Room> roomList = roomRepository.getUnoccupiedRoomsByApartment(startDate, finishDate, apartmentId);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for(Room room : roomList){
            roomDTOList.add(roomMapper.getRoomDTO(room));
        }
        return roomDTOList;
    }


    public Boolean checkIfModified(Long roomID, String version){
        Room room = roomRepository.findById(roomID);
        String current_version = room.getVersion().toString();
        return !version.equals(current_version);
    }
}
