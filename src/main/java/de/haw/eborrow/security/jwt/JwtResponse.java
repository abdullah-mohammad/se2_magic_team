package de.haw.eborrow.security.jwt;

import java.util.Date;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String profilepicture;
    private Date birthdate;


    public JwtResponse(String accessToken, Long id, String username, String firstname, String lastname, String email, String gender, Date birthdate, String profilepicture) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
        this.profilepicture = profilepicture;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}