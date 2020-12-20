package de.haw.eborrow.controller;

import de.haw.eborrow.models.User;
import de.haw.eborrow.repository.UserRepository;
import de.haw.eborrow.security.SigninRequest;
import de.haw.eborrow.security.SignupRequest;
import de.haw.eborrow.security.jwt.JwtResponse;
import de.haw.eborrow.security.jwt.JwtUtils;
import de.haw.eborrow.services.FilesStorageService;
import de.haw.eborrow.services.UserDetailsImpl;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    private UserRepository applicationUserRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository applicationUserRepository,
                          PasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@ModelAttribute SignupRequest userRequest,@RequestParam(value = "profilepicture",
            required = false) MultipartFile  profilepicture) {

        try {
            userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
            String fileName = "";
            if (profilepicture != null) {
                fileName = userRequest.getUsername() + "_" + StringUtils.cleanPath(
                        Objects.requireNonNull(profilepicture.getOriginalFilename()));
                storageService.save(profilepicture, fileName,"profilepictures/");
            }
            Date birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(userRequest.getBirthdate());
            User _user = applicationUserRepository.save(new User(userRequest.getUsername(),
                    userRequest.getPassword(), userRequest.getFirstname(), userRequest.getLastname()
                    , userRequest.getEmail(), userRequest.getGender(), birthdate, fileName));

            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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