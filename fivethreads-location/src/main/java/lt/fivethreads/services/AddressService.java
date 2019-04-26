package lt.fivethreads.services;

import lt.fivethreads.entities.Address;

public interface AddressService {
    String getCombinedAddress(Address address);
    Boolean compareTwoAddress (Address address1, Address address2);
}
