package lt.fivethreads.repositories;

import lt.fivethreads.entities.Room;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class RoomRepositoryImpl implements RoomRepository {

    @PersistenceContext
    EntityManager em;

    public Room findById(long id) {
        return em.find(Room.class, id);
    }

    public List<Room> getAll() {
        return em.createNamedQuery("Room.FindAll",
        Room.class).getResultList();
    }

    public void deleteRoom(long id) {
        Room room_to_delete = em.find(Room.class, id);
        em.remove(room_to_delete);
    }

    public Room updateRoom(Room room) {
        return em.merge(room);
    }

    public Room createRoom(Room room) {
        em.persist(room);
        em.flush();//auto generated id only at flush time
        return room;
    }

    public boolean existsByNumberAndId(Long number, Long id) {
        return em.createNamedQuery("Room.ExistsByNumberAndApartmentId")
                .setParameter("apartment_ID", id)
                .setParameter("number", number)
                .getResultList().size() == 1;
    }

    public List<Room> getUnoccupiedRooms(Date startDate, Date finishDate) {
        return em.createNamedQuery("Room.FindAllUnoccupiedRooms", Room.class)
                .setParameter("startDate", startDate)
                .setParameter("finishDate", finishDate)
                .getResultList();
    }

    public List<Room> getUnoccupiedRoomsByApartment(Date startDate, Date finishDate, Long apartmentId) {
        return em.createNamedQuery("Room.FindUnoccupiedRoomsByApartmentId", Room.class)
                .setParameter("startDate", startDate)
                .setParameter("finishDate", finishDate)
                .setParameter("apartment_ID", apartmentId)
                .getResultList();
    }
    public List<Room> getUnoccupiedRoomByCity(Date startDate, Date finishDate, String city) {

        return em.createNamedQuery("Room.FindUnoccupiedRoomsByCity", Room.class)
                .setParameter("startDate", startDate)
                .setParameter("finishDate", finishDate)
                .setParameter("city", city)
                .getResultList();
    }

}
