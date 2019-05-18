package lt.fivethreads.services;

import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;

import java.util.Date;
import java.util.List;

public interface RoomService {
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Long id);
    RoomDTO updateRoom (RoomDTO room);
    void deleteRoom(Long id);
    RoomDTO createRoom (RoomForm room);
    boolean checkIfRoomExists(String name, Long apartmentId);
    List <RoomDTO> getAllUnoccupiedAccommodations(Date startDate, Date finishDate);
    List <RoomDTO> getAllUnoccupiedAccommodationsByApartmentId (Date startDate, Date finishDate, Long apartmentId);
}
