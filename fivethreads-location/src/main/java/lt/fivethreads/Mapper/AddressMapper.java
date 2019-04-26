package lt.fivethreads.Mapper;

import lt.fivethreads.entities.Address;
import lt.fivethreads.entities.request.FullAddressDTO;
import lt.fivethreads.entities.request.ShortAddressDTO;
import lt.fivethreads.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    @Autowired
    AddressService addressService;

    public Address convertFullAddressToAddress(FullAddressDTO fullAddressDTO){
        Address address = new Address();
        address.setCity(fullAddressDTO.getCity());
        address.setStreet(fullAddressDTO.getStreet());
        address.setCountry(fullAddressDTO.getCountry());
        address.setHouseNumber(fullAddressDTO.getHouseNumber());
        address.setFlatNumber(fullAddressDTO.getFlatNumber());
        address.setLatitude(fullAddressDTO.getLatitude());
        address.setLongitude(fullAddressDTO.getLongitude());
        return address;
    }

    public ShortAddressDTO convertAddressToShortAddress(Address address){
        ShortAddressDTO shortAddress = new ShortAddressDTO();
        shortAddress.setFullAddress(addressService.getCombinedAddress(address));
        shortAddress.setLatitude(address.getLatitude());
        shortAddress.setLongitude(address.getLongitude());
        return  shortAddress;
    }

    public Address copyInstance(Address address){
        Address newAddres = new Address();
        newAddres.setStreet(address.getStreet());
        newAddres.setLongitude(address.getLongitude());
        newAddres.setLatitude(address.getLatitude());
        newAddres.setFlatNumber(address.getFlatNumber());
        newAddres.setHouseNumber(address.getHouseNumber());
        newAddres.setCountry(address.getCountry());
        newAddres.setCity(address.getCity());
        return newAddres;
    }
}
