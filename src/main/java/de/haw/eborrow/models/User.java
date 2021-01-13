package de.haw.eborrow.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd" , shape = JsonFormat.Shape.STRING , timezone="Europe/Zagreb")
    private Date birthdate;
    private String profilepicture;

    //@JsonManagedReference("user")
    @OneToMany(mappedBy="user")
    Set<Item> items = new HashSet();

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy="user")
    private List<Borrow> borrowedItems = new ArrayList<>();

    public User() {
    }

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
    }

    public User(String _username, String _password, String _firstName, String _lastName, String _email, String _gender, Date _birthdate,String _profilepicture, Address address) {
        this.username = _username;
        this.password = _password;
        this.firstname = _firstName;
        this.lastname = _lastName;
        this.email = _email;
        this.gender = _gender;
        this.birthdate = _birthdate;
        this.address = address;
        this.profilepicture = _profilepicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void addItem(Item item ){
        this.items.add(item);
//        item.setUser(this);
    }

    public void addItemToBorrowedItems(Borrow borrow){
        this.borrowedItems.add(borrow);
    }
}