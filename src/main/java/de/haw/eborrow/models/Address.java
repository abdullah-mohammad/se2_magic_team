package de.haw.eborrow.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String street;
    private String streetnumber;
    private int zipcode;
    private String city;
    private String country;

    @OneToMany(mappedBy="address")
    Set<User> users = new HashSet();

    public Address() { }

    public Address(String street, String streetnumber, int zipcode, String city, String country) {
        this.street = street;
        this.streetnumber = streetnumber;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }


    // getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNr() {
        return streetnumber;
    }

    public void setStreetNr(String streetNr) {
        this.streetnumber = streetNr;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addUser(User user ){
        this.users.add(user);
    }
}
