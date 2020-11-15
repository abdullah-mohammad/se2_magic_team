package de.haw.eborrow.controller;

import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private PasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository applicationUserRepository,
                          PasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignupRequest userRequest) {
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

        Date birthdate;
        try {
            birthdate=new SimpleDateFormat("dd/MM/yyyy").parse(userRequest.getBirthdate());
            logger.warn(birthdate.toString());
        } catch (ParseException e) {
            System.out.println(e);
            birthdate = null;
        }
        User _user = applicationUserRepository.save(new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname(), userRequest.getEmail(), userRequest.getGender(), birthdate));
        return new ResponseEntity<>(_user, HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
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

    @PreAuthorize("permitAll()")
    @PutMapping("/edit-user/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = applicationUserRepository.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername(user.getUsername());
            _user.setEmail(user.getEmail());
            _user.setFirstname(user.getFirstname());
            _user.setLastname(user.getLastname());
            _user.setGender(user.getGender());
            _user.setBirthdate(user.getBirthdate());
            return new ResponseEntity<User>(applicationUserRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("permitAll()")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        try {
            Optional<User> user = applicationUserRepository.findById(id);

            if (user.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}