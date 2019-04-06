package lt.fivethreads.repositories;

import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;
import lt.fivethreads.entities.User;
import lt.fivethreads.exception.WrongTripData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class TripMemberRepositoryImplementation implements TripMemberRepository {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CarTicketRepository carTicketRepository;

    @Autowired
    TripAccommodationRepository tripAccommodationRepository;


    @Autowired
    UserRepository userRepository;

    public void saveTripMember(TripMember tripMember) {
        em.persist(tripMember);
    }

    public Boolean checkIfExistByID(Long id){
        if(em.find(TripMember.class, id)==null){
            return false;
        }
        return true;
    }
    public void updateTripMember(TripMember tripMember) {
        TripMember tripMemberOld =em.find(TripMember.class, tripMember.getId());
        if (tripMember.getCarTicket() != null) {
            if (tripMemberOld.getCarTicket()==null){
                carTicketRepository.saveCarTicket(tripMember.getCarTicket());
            }
            else{
                tripMember.getCarTicket().setId(tripMemberOld.getCarTicket().getId());
            }
        }
        else{
            if(tripMemberOld.getCarTicket()!=null){
                em.remove(tripMemberOld.getCarTicket());
            }
        }
        if (tripMember.getTripAccommodation() != null) {
            if (tripMemberOld.getTripAccommodation()==null){
                tripAccommodationRepository.saveTripAccommodation(tripMember.getTripAccommodation());
            }
            else{
                tripMember.getTripAccommodation().setId(tripMemberOld.getTripAccommodation().getId());
            }
        }
        else{
            if(tripMemberOld.getTripAccommodation()!=null){
                em.remove(tripMemberOld.getTripAccommodation());
            }
        }
        em.merge(tripMember);
    }

    public TripMember findById(Long id) {
        return em.find(TripMember.class, id);
    }

    public TripMember getTripMemberByTripIDAndEmail(Long tripID, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new WrongTripData("TripMember does not exist."));
        Long user_id = user.getId();
        return em.createNamedQuery("TripMember.findByTripIDEmail", TripMember.class).setParameter("tripID", tripID)
                .setParameter("user_id", user_id)
                .getSingleResult();
    }

    public void addCancellation(TripCancellation tripCancellation){
        em.persist(tripCancellation);
        em.merge(tripCancellation.getTripMember());
    }
}
