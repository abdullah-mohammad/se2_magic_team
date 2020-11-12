package de.haw.eborrow.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String gender;
    private String profilepicture;
    private Date birthdate;

    @JsonManagedReference("user")
    @OneToMany(mappedBy="user")
    Set<Item> items = new HashSet();

    public User() {
    }

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
    }

    public User(String _username, String _password, String _email, String _gender, Date _birthdate) {
        this.username = _username;
        this.password = _password;
        this.email = _email;
        this.gender = _gender;
        this.birthdate = _birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void addItem(Item item ){
        this.items.add(item);
//        item.setUser(this);
    }
}