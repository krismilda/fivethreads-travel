package lt.fivethreads.services;

import lt.fivethreads.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressServiceImplementation implements AddressService
{
    public String getCombinedAddress(Address address){
        String fullAddress = address.getStreet()+" str. "+address.getHouseNumber();
        if(address.getFlatNumber()!=null){
            fullAddress +="-"+address.getFlatNumber();
        }
        return fullAddress+", "+address.getCity()+", "+address.getCountry();
    }

    public Boolean compareTwoAddress (Address address1, Address address2){
        return address1.getCountry().equals(address2.getCountry())
                && address1.getCity().equals(address2.getCity())
                && address1.getStreet().equals(address2.getStreet())
                && address1.getHouseNumber().equals(address2.getHouseNumber())
                && address1.getFlatNumber().equals(address2.getFlatNumber());
    }

}
