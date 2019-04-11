package lt.fivethreads.services;

import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;

import java.util.List;

public interface RoomService {
    List<RoomDTO> getAllRooms();

    RoomDTO getRoomById(Long id);

    RoomDTO updateRoom (RoomDTO room);

    void deleteRoom(Long id);

    RoomDTO createRoom (RoomForm room);

    boolean checkIfRoomExists(Long number, Long apartmentId);
}
