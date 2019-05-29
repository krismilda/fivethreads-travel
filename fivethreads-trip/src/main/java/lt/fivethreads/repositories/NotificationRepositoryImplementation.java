package lt.fivethreads.repositories;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.TripMemberHistory;
import lt.fivethreads.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class NotificationRepositoryImplementation implements NotificationRepository {
    @PersistenceContext
    EntityManager em;

    public void saveNotification(Notification notification) {
        em.persist(notification.getTripHistory().getArrival());
        em.persist(notification.getTripHistory().getDeparture());
        em.persist(notification.getTripHistory());
        em.persist(notification);
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
        ) {
            em.merge(tripMemberHistory);
        }
    }

    public List<Notification> getAllUserNotificationByEmail(String email) {
        return em.createNamedQuery("Notification.FindAllUserByEmail", Notification.class)
                .setParameter("email", email)
                .getResultList();
    }

    public Notification getNotificationByID(Long id) {
        return em.createNamedQuery("Notification.FindByID", Notification.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Notification> getAllOrganizerNotificationByEmail(String email){
        return em.createNamedQuery("Notification.FindAllOrganizerByEmail", Notification.class)
                .setParameter("email", email)
                .getResultList();
    }
    public void updateNotification(Notification notification) {
        em.merge(notification);
    }

    public void deleteUser(User user){
        List<Notification> notificationList = this.getAllUserNotificationByEmail(user.getEmail());
        for ( Notification notification:notificationList
             ) {
            em.remove(notification);
        }
        em.remove(user);
    }
}
