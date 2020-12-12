package de.haw.eborrow.security;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 40)
    private String firstname;

    @NotBlank
    @Size(min = 6, max = 40)
    private String lastname;

    @NotBlank
    private String email;

    @NotBlank
    private String gender;

    @NotBlank
    private String birthdate;

    private MultipartFile profilepicture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {return email; }

    public void setEmail(String email) { this.email = email; }

    public String getFirstname() {return firstname; }

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() {return lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getBirthdate() { return birthdate; }

    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public MultipartFile getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(MultipartFile profilepicture) {
        this.profilepicture = profilepicture;
    }

}