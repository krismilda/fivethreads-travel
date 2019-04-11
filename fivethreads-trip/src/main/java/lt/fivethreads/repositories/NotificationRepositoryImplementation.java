package lt.fivethreads.repositories;

import lt.fivethreads.entities.Notification;
import lt.fivethreads.entities.TripMemberHistory;
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
        em.persist(notification.getTripHistory());
        em.persist(notification);
        for (TripMemberHistory tripMemberHistory : notification.getTripHistory().getTripMembers()
        ) {

            em.merge(tripMemberHistory);
        }

    }

    public List<Notification> getAllNotificationByEmail(String email) {
        return em.createNamedQuery("Notification.FindAllByEmail", Notification.class)
                .setParameter("email", email)
                .getResultList();
    }
}
