package lt.fivethreads.repository;

import lt.fivethreads.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class AddressRepositoryImplementation implements AddressRepository
{
    @Autowired
    EntityManager em;
    public Address findByID(Long id){
        return em.find(Address.class, id);
    }
}
