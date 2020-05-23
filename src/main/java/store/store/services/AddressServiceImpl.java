package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.entity.Address;
import store.store.repository.AddressRepository;

import java.util.List;


@Service
public class AddressServiceImpl {

    AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }
}
