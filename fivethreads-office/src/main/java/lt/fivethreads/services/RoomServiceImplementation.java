package lt.fivethreads.services;

import lt.fivethreads.entities.Apartment;
import lt.fivethreads.entities.Office;
import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;
import lt.fivethreads.mapper.RoomMapper;
import lt.fivethreads.repositories.ApartmentRepository;
import lt.fivethreads.repositories.RoomRepository;
import lt.fivethreads.validation.DateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    @Autowired
    ApartmentRepository apartmentRepository;

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
        room.setName(roomDTO.getName());
        room.setCapacity(roomDTO.getCapacity());
        room.setApartment(apartmentRepository.findById(roomDTO.getApartmentId()));
        return roomRepository.updateRoom(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteRoom(id); }

    public Room createRoom (RoomForm roomForm) {

        if (roomForm.getName() == null){
            try {

                String defaultName = roomRepository.findLastDefaultName(roomForm.getApartmentId());

                defaultName = defaultName.substring(0, defaultName.indexOf(".")+1)
                        + (Integer.parseInt(defaultName.substring(defaultName.indexOf(".") + 1))+1);
                roomForm.setName(defaultName);
            }catch(NoResultException ex){{
                roomForm.setName("Kambarys Nr.1");
            }}
        }

        Room room_to_save = roomMapper.convertRegisteredRoomToRoom(roomForm);

        return roomRepository.createRoom(room_to_save);
    }

    public boolean checkIfRoomExists(String name, Long apartmentId) {
        return roomRepository.existsByNameAndId(name, apartmentId);
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
