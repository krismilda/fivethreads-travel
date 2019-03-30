package lt.fivethreads.controller;

import lt.fivethreads.entities.request.ApartmentDTO;
import lt.fivethreads.entities.request.ApartmentForm;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;
import lt.fivethreads.services.ApartmentService;
import lt.fivethreads.services.OfficeService;
import lt.fivethreads.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public List<RoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public RoomDTO getRoomById(@PathVariable("roomId") int roomId) {
        long id = roomId;
        return roomService.getRoomById(id);
    }

    @DeleteMapping("/admin/rooms/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") int roomId) {
        long id = roomId;
        roomService.deleteRoom(id);
        return new ResponseEntity<>("Room deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/rooms/room")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> updateRoom(@Validated @RequestBody RoomDTO roomDTO) {
        roomService.updateRoom(roomDTO);
        return new ResponseEntity<>("Room updated successfully!", HttpStatus.OK);
    }

    @PostMapping("/admin/rooms/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerRoom(@Validated @RequestBody RoomForm roomForm) {
        if (roomForm== null) {
            return new ResponseEntity<>("Fail -> RoomForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (roomService.checkIfRoomExists(roomForm.getNumber(),
                roomForm.getApartmentId())) {
            return new ResponseEntity<>("Fail -> Room is already created!",
                    HttpStatus.BAD_REQUEST);
        }

        roomService.createRoom(roomForm);
        return new ResponseEntity<>("Room created successfully!", HttpStatus.OK);
    }
}
