package de.haw.eborrow.controller;

import de.haw.eborrow.models.Address;
import de.haw.eborrow.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

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

}
