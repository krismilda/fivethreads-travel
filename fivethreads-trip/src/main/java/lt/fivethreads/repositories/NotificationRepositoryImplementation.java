package lt.fivethreads.repositories;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.TripMemberHistory;
import lt.fivethreads.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

    public List<Notification> getAllUserNotificationByEmailPage(String email, int from, int amount) {
        Query query = em.createNamedQuery("Notification.FindAllUserByEmail", Notification.class);
        query.setMaxResults(amount);
        query.setFirstResult(from);
        List<Notification> notificationList= query.setParameter("email", email)
                .getResultList();
        return notificationList;
    }

    public List<Notification> getAllOrganizerNotificationByEmailPage(String email, int from, int amount) {
        Query query = em.createNamedQuery("Notification.FindAllOrganizerByEmail", Notification.class);
        query.setMaxResults(amount);
        query.setFirstResult(from);
        List<Notification> notificationList= query.setParameter("email", email)
                .getResultList();
        return notificationList;
    }

    public long getCountNotificationByEmailUser(String email) {
        Query query = em.createNamedQuery("Notification.CountUser");
        long count = (long) query.setParameter("email", email)
               .getSingleResult();
        return count;
    }

    public long getCountNotificationByEmailOrganizer(String email) {
        Query query = em.createNamedQuery("Notification.CountOrganizer");
        long count = (long) query.setParameter("email", email)
                .getSingleResult();
        return count;
    }

    public Notification getNotificationByID(Long id) {
        return em.createNamedQuery("Notification.FindByID", Notification.class)
                .setParameter("id", id)
                .getSingleResult();
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
    public List<Notification> getAllUserNotificationByEmail(String email) {
        return em.createNamedQuery("Notification.FindAllUserByEmail", Notification.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<Notification> getAllOrganizerNotificationByEmail(String email){
        return em.createNamedQuery("Notification.FindAllOrganizerByEmail", Notification.class)
                .setParameter("email", email)
                .getResultList();
    }
}
