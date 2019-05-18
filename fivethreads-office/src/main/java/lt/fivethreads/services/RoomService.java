package lt.fivethreads.services;

import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;

import java.util.Date;
import java.util.List;

public interface RoomService {
    List<RoomDTO> getAllRooms();
    Room getRoomById(Long id);
    Room updateRoom (RoomDTO room);
    void deleteRoom(Long id);
    Room createRoom (RoomForm room);
    boolean checkIfRoomExists(Long number, Long apartmentId);
    List <RoomDTO> getAllUnoccupiedAccommodations(Date startDate, Date finishDate);
    List <RoomDTO> getAllUnoccupiedAccommodationsByApartmentId (Date startDate, Date finishDate, Long apartmentId);
    Boolean checkIfModified(Long roomID, String version);
}
