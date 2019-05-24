package lt.fivethreads.repositories;

import lt.fivethreads.entities.*;
import lt.fivethreads.exception.WrongTripData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TripMemberRepositoryImplementation implements TripMemberRepository {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    CarTicketRepository carTicketRepository;

    @Autowired
    TripAccommodationRepository tripAccommodationRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    public void saveTripMember(TripMember tripMember) {
        if(tripMember.getCarTicket() != null){
            carTicketRepository.saveCarTicket(tripMember.getCarTicket());
        }
        if(tripMember.getTripAccommodation() != null){
            if(tripMember.getTripAccommodation().getAccommodationType() == AccommodationType.DEVRIDGE_APARTAMENTS) {

                List<Room> room = roomRepository.getUnoccupiedRoomByCity(tripMember.getTripAccommodation().getAccommodationStart(),
                        tripMember.getTripAccommodation().getAccommodationFinish(), tripMember.getTrip().getArrival().getCity());
                if (room.size() == 0)
                    throw new WrongTripData("Failed to accommodate user. No more free DevBridge rooms in " +
                            tripMember.getTrip().getArrival().getCity());
                tripMember.getTripAccommodation().setRoom(room.get(0));
                tripMember.getTripAccommodation().setHotelAddress(room.get(0).getApartment().getAddress());
            }
                tripAccommodationRepository.saveTripAccommodation(tripMember.getTripAccommodation());

        }
        em.persist(tripMember);
    }

    public Boolean checkIfExistByID(Long id) {
        if (em.find(TripMember.class, id) == null) {
            return false;
        }
        return true;
    }

    public void updateTripMember(TripMember tripMember) {
        TripMember tripMemberOld = em.find(TripMember.class, tripMember.getId());
        if (tripMember.getCarTicket() != null) {
            if (tripMemberOld.getCarTicket() == null) {
                carTicketRepository.saveCarTicket(tripMember.getCarTicket());
            } else {
                tripMember.getCarTicket().setId(tripMemberOld.getCarTicket().getId());
            }
        } else {
            if (tripMemberOld.getCarTicket() != null) {
                em.remove(tripMemberOld.getCarTicket());
            }
        }
        if (tripMember.getTripAccommodation() != null) {
            if (tripMemberOld.getTripAccommodation() == null) {
                tripAccommodationRepository.saveTripAccommodation(tripMember.getTripAccommodation());
            } else {
                tripMember.getTripAccommodation().setId(tripMemberOld.getTripAccommodation().getId());
            }
        } else {
            if (tripMemberOld.getTripAccommodation() != null) {
                em.remove(tripMemberOld.getTripAccommodation());
            }
        }
        if (tripMember.getFlightTicket() != null) {
            if (tripMemberOld.getFlightTicket() == null) {
                em.persist(tripMemberOld.getFlightTicket());
            } else {
                tripMember.getFlightTicket().setId(tripMemberOld.getFlightTicket().getId());
            }
        } else {
            if (tripMemberOld.getFlightTicket() != null) {
                em.remove(tripMemberOld.getFlightTicket());
            }
        }
        em.merge(tripMember);
    }

    public void removeCarTicket (CarTicket carTicket){
        em.remove(carTicket);
    }
    public void removeTripAccomodation (TripAccommodation tripAccommodation){
        em.remove(tripAccommodation);
    }
    public void saveFlightTicket(TripMember tripMember) {
        if (tripMember.getFlightTicket() != null) {
            em.persist(tripMember.getFlightTicket());
            em.merge(tripMember);
        }
    }


    public void saveCarTicket(TripMember tripMember) {
        if (tripMember.getCarTicket() != null) {
            em.persist(tripMember.getCarTicket());
            em.merge(tripMember);
        }
    }

    public void saveAccommodationTicket(TripMember tripMember) {
        if (tripMember.getTripAccommodation() != null) {
            em.persist(tripMember.getTripAccommodation());
            em.merge(tripMember);
        }
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

    public TripMember findByFlightFileID(Long fileID) {
        return em.createNamedQuery("TripMember.findByFlightFileID", TripMember.class)
                .setParameter("fileID", fileID)
                .getSingleResult();
    }

    public TripMember findByCarFileID(Long fileID) {
        return em.createNamedQuery("TripMember.findByCarFileID", TripMember.class)
                .setParameter("fileID", fileID)
                .getSingleResult();
    }

    public TripMember findByAccommodationFileID(Long fileID) {
        return em.createNamedQuery("TripMember.findByAccommodationFileID", TripMember.class)
                .setParameter("fileID", fileID)
                .getSingleResult();
    }

    public void deleteTripMember(TripMember tripMember){
        if(tripMember.getTripAccommodation()!=null){
            em.remove(tripMember.getTripAccommodation());
        }
        if(tripMember.getCarTicket()!=null){
            em.remove(tripMember.getCarTicket());
        }
        if(tripMember.getFlightTicket()!=null){
            em.remove(tripMember.getFlightTicket());
        }
        em.remove(tripMember);
    }
}
