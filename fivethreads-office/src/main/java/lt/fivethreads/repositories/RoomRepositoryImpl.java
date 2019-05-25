package lt.fivethreads.repositories;

import lt.fivethreads.entities.Room;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
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
        try{
            em.detach(room);
            return em.merge(room);
        }
        catch (OptimisticLockException e){
            throw new OptimisticLockException("Room was modified.");
        }
    }

    public Room createRoom(Room room) {
        em.persist(room);
        em.flush();//auto generated id only at flush time
        return room;
    }

    public boolean existsByNameAndId(String name, Long id) {
        return em.createNamedQuery("Room.ExistsByNameAndApartmentId")
                .setParameter("apartment_ID", id)
                .setParameter("name", name)
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

    public String findLastDefaultName(Long apartmentId) throws NoResultException {
    /*JPQL does not have the necessary syntax for order by cast .. statement
    * That's why a NativeQuery is used
    * Reason why the type is Object and no return class is specified
    * https://stackoverflow.com/questions/19051058/hibernate-createnativequery-with-non-entity-class-result
    * */
        Object defaultName = em.createNativeQuery("SELECT r.NAME FROM ROOM AS r " +
                "WHERE r.APARTMENT_ID = ?1 AND r.NAME LIKE ?2 " +
                "ORDER BY CAST(SUBSTRING(r.Name, 13) AS INT) DESC")
                .setParameter(1, apartmentId)
                .setParameter(2, "Kambarys Nr.%_")
                .setMaxResults(1)
                .getSingleResult();

        return (String) defaultName;
    }

}
