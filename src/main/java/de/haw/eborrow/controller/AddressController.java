package de.haw.eborrow.controller;

import de.haw.eborrow.models.Address;
import de.haw.eborrow.models.AddressDTO;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.AddressRepository;
import de.haw.eborrow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAllAddresses() {
        try {
            List<Address> addresses = new ArrayList<>();
            addresses.addAll(addressRepository.findAll());
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(@ModelAttribute AddressDTO addressDTO) {
        try {
            String street = addressDTO.getStreet();
            String streetnumber = addressDTO.getStreetnumber();
            String zipcode = addressDTO.getZipcode();
            String city = addressDTO.getCity();
            String country = addressDTO.getCountry();
            User user = userRepository.getOne(Long.valueOf(addressDTO.getUserId()));
            Address savedAddress = addressRepository.save(new Address(street, streetnumber, Integer.parseInt(zipcode), city, country, user));
            return new ResponseEntity<>(savedAddress, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
