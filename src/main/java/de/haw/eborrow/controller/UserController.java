package de.haw.eborrow.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.haw.eborrow.models.Address;
import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.AddressRepository;
import de.haw.eborrow.repository.ItemRepository;
import de.haw.eborrow.repository.UserRepository;
import de.haw.eborrow.security.SigninRequest;
import de.haw.eborrow.security.SignupRequest;
import de.haw.eborrow.security.jwt.JwtResponse;
import de.haw.eborrow.security.jwt.JwtUtils;
import de.haw.eborrow.services.FilesStorageService;
import de.haw.eborrow.services.UserDetailsImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
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

    @Autowired
    FilesStorageService storageService;

    @Autowired
    ItemRepository itemRepository;

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
    public ResponseEntity<User> signUp(@ModelAttribute SignupRequest userRequest,@RequestParam(value = "profilepicture",
            required = false) MultipartFile  profilepicture) {
        Date birthdate;
        try {
            if (userRequest.getPassword().length()<5){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
            String fileName = "";
            if (profilepicture != null) {
                fileName = userRequest.getUsername() + "_" + StringUtils.cleanPath(
                        Objects.requireNonNull(profilepicture.getOriginalFilename()));
                storageService.save(profilepicture, fileName,"profilepictures/");
            }
            birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(userRequest.getBirthdate());
            User _user = applicationUserRepository.save(new User(userRequest.getUsername(),
                    userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname()
                    , userRequest.getEmail(), userRequest.getGender(), birthdate, fileName, null));

            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest signinRequest) {
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jwtUtils.generateJwtToken(auth);

            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), userDetails.getGender(), userDetails.getBirthdate(), userDetails.getProfilepicture()));

        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping("/try")
    public ResponseEntity<List<User>> getAllUsers() {
            return (ResponseEntity<List<User>>) applicationUserRepository.findAll();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        try {
            String name = "";
            Optional<User> user = applicationUserRepository.findById(id);
            if (user.isPresent()) {
                List<Item> items = itemRepository.findAllByUser(user.get());
                for (Item item:items) {
                    itemRepository.delete(item);
                }
                name = user.get().getFirstname();
                storageService.delete("/profilepictures/",user.get().getProfilepicture());
                applicationUserRepository.deleteById(id);
                return new ResponseEntity<>(name,HttpStatus.OK);
            }

            return new ResponseEntity<>( HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<User> editUser(@PathVariable("id") long id,
                                         @RequestParam(value = "newPic", required = false) MultipartFile newUserPic,
                                         @RequestParam(value = "editPass") Boolean shouldEditPass,
                                         @RequestParam(value = "username") String username,
                                         @RequestParam(value = "password") String password,
                                         @RequestParam(value = "firstname") String firstname,
                                         @RequestParam(value = "lastname") String lastname,
                                         @RequestParam(value = "email") String email,
                                         @RequestParam(value = "gender") String gender,
                                         @RequestParam(value = "birthdate") String birthdate) throws ParseException {
        Optional<User> userData = applicationUserRepository.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername(username);
            if (shouldEditPass)
                _user.setPassword(bCryptPasswordEncoder.encode(password));
            _user.setEmail(email);
            _user.setFirstname(firstname);
            _user.setLastname(lastname);
            _user.setGender(gender);
            Date userBirthdate = new SimpleDateFormat("yyyy-MM-dd").parse(birthdate);
            _user.setBirthdate(userBirthdate);
            String fileName = "";
            if (newUserPic != null) {
                fileName = username + "_" + id + "." + FilenameUtils.getExtension(newUserPic.getOriginalFilename());
                String directory = "profilepictures/";
                try{
                    storageService.delete("/profilepictures/", fileName);
                } catch (Exception ignored){}
                storageService.save(newUserPic, fileName,directory);
                _user.setProfilepicture(fileName);
            }

            return new ResponseEntity<User>(applicationUserRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/edit-user-address/{id}")
    public ResponseEntity<User> editUserAddress(@PathVariable("id") long id,
                                                @RequestBody long addressId) {
        try {
            Optional<User> user = applicationUserRepository.findById(id);
            if (user.isPresent()) {
                User _user = user.get();
                Address address = addressRepository.findById(addressId).orElse(null);
                _user.setAddress(address);
                return new ResponseEntity<>(applicationUserRepository.save(_user), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

            if (inlineAddr == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<String>(inlineAddr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(
            value = "/get-img/{pic:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE}
    )
    public byte[] getImageWithMediaTyp(@PathVariable("pic") String pic) {
        InputStream in = null;
        try {
            in = storageService.load("/profilepictures/",pic).getInputStream();
            return IOUtils.toByteArray(in);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}