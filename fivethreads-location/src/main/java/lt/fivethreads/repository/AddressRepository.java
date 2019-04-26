package lt.fivethreads.repository;

import lt.fivethreads.entities.Address;

public interface AddressRepository {
    Address findByID(Long id);
}
