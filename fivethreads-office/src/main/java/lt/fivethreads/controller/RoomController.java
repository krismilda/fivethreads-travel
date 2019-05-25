package lt.fivethreads.controller;


import lt.fivethreads.entities.Room;
import lt.fivethreads.entities.request.DateForm;
import lt.fivethreads.entities.request.RoomDTO;
import lt.fivethreads.entities.request.RoomForm;

import lt.fivethreads.mapper.RoomMapper;
import lt.fivethreads.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomMapper roomMapper;

    @GetMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANISER')")
    public ResponseEntity<?> getRoomById(@PathVariable("roomId") int roomId) {
        long id = roomId;
        Room room = roomService.getRoomById(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomMapper.getRoomDTO(room));
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
    public ResponseEntity<?> updateRoom(@Validated @RequestBody RoomDTO roomDTO, WebRequest request) {
        Room updatedRoom = roomService.updateRoom(roomDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomMapper.getRoomDTO(updatedRoom));
    }

    @PostMapping("/admin/rooms/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerRoom(@Validated @RequestBody RoomForm roomForm) {
        if (roomForm == null) {
            return new ResponseEntity<>("Fail -> RoomForm is null!",
                    HttpStatus.BAD_REQUEST);
        }

        if (roomService.checkIfRoomExists(roomForm.getName(),
                roomForm.getApartmentId())) {
            return new ResponseEntity<>("Fail -> Room is already created!",
                    HttpStatus.BAD_REQUEST);
        }

        Room createdRoom = roomService.createRoom(roomForm);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomMapper.getRoomDTO(createdRoom));
    }

    @GetMapping("/rooms/unoccupied")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodations(@Validated @RequestBody DateForm form){

        return new ResponseEntity<>(roomService.getAllUnoccupiedAccommodations(form.getStartDate(), form.getFinishDate()), HttpStatus.OK);
    }
    @GetMapping("/rooms/unoccupied/{apartmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedAccommodationsByApartmentId(@Validated @RequestBody DateForm form,
                                                                   @PathVariable("apartmentId") int apartmentId){
        long id = apartmentId;

        return new ResponseEntity<>(roomService.getAllUnoccupiedAccommodationsByApartmentId(
                form.getStartDate(), form.getFinishDate(), id), HttpStatus.OK);
    }

    @PostMapping("/rooms/massCreate/{apartmentId}&{numberOfRooms}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity massCreateRooms(@PathVariable Long apartmentId, @PathVariable int numberOfRooms){

        List<RoomForm> roomsToCreate = new ArrayList<>();

        for (int i = 0; i < numberOfRooms; i++){
            RoomForm form = new RoomForm();
            form.setApartmentId(apartmentId);
            form.setCapacity((long) 1);
            roomsToCreate.add(form);
        }

        List<RoomDTO> createdRooms = new ArrayList<>();

        for (RoomForm form:roomsToCreate) {
            createdRooms.add(roomMapper.getRoomDTO(roomService.createRoom(form)));
        }

        return new ResponseEntity<>(createdRooms, HttpStatus.CREATED);
    }

    @GetMapping("rooms/unccupiedInCity/{city}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity getUnoccupiedRoomsInCity(@Validated @RequestBody DateForm dateForm,
                                                       @PathVariable String city){

        List<RoomDTO> unoccupiedRooms = roomService.getUnoccupiedAccommodationByTripMember(dateForm, city);

        return new ResponseEntity<>(unoccupiedRooms, HttpStatus.CREATED);
    }

}
