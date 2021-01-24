package de.haw.eborrow.models;

import javax.validation.constraints.NotBlank;

public class AddressDTO {
    @NotBlank
    private String street;

    @NotBlank
    private String streetnumber;

    @NotBlank
    private String zipcode;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String userId;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
