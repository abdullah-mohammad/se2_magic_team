package de.haw.eborrow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.haw.eborrow.models.Address;
import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.AddressRepository;
import de.haw.eborrow.repository.UserRepository;
import de.haw.eborrow.security.SigninRequest;
import de.haw.eborrow.security.SignupRequest;
import de.haw.eborrow.security.jwt.JwtResponse;
import de.haw.eborrow.security.jwt.JwtUtils;
import de.haw.eborrow.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    private UserRepository applicationUserRepository;
    private AddressRepository addressRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository applicationUserRepository,
                          PasswordEncoder bCryptPasswordEncoder,
                          AddressRepository addressRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.addressRepository = addressRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignupRequest userRequest) {
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

        Date birthdate;
        //TODO: Adress Felder in Signup Form
        Optional<Address> address = addressRepository.findById(1L);
        try {
            birthdate=new SimpleDateFormat("dd/MM/yyyy").parse(userRequest.getBirthdate());
            logger.warn(birthdate.toString());
        } catch (ParseException e) {
            System.out.println(e);
            birthdate = null;
        }
        User _user = applicationUserRepository.save(new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getEmail(), userRequest.getGender(), birthdate, address.get()));
        return new ResponseEntity<>(_user, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest signinRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtils.generateJwtToken(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), userDetails.getGender(), userDetails.getBirthdate(), userDetails.getProfilepicture()));

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        try {
            Optional<User> user = applicationUserRepository.findById(id);

            if (!user.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("permitAll()")
    @PutMapping("/edit-user/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") long id, @RequestBody Map<String,Object> body) throws ParseException {
        Optional<User> userData = applicationUserRepository.findById(id);
        boolean shouldEditPass = (boolean) body.get("editPass");
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername((String) body.get("username"));
            if (shouldEditPass)
                _user.setPassword(bCryptPasswordEncoder.encode((String) body.get("password")));
            _user.setEmail((String) body.get("email"));
            _user.setFirstname((String) body.get("firstname"));
            _user.setLastname((String) body.get("lastname"));
            _user.setGender((String) body.get("gender"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date date = formatter.parse(body.get("birthdate").toString());
            _user.setBirthdate(date);
            return new ResponseEntity<User>(applicationUserRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/check-pass/{id}")
    public boolean checkUserPass(@PathVariable("id") long id, @RequestBody String pass) {
        String userCurrentPass = applicationUserRepository.getUserPass(id);
        return bCryptPasswordEncoder.matches(pass, userCurrentPass);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/user-address/{id}")
    public ResponseEntity<String> getUserInlineAddress(@PathVariable("id") long id) {
        try {
            String inlineAddr = applicationUserRepository.getUserAddress(id);

            if (inlineAddr==null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<String>(inlineAddr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}