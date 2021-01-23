package de.haw.eborrow.models;

import javax.validation.constraints.NotBlank;

public class ItemDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String availableFrom;

    @NotBlank
    private String availableTo;

    @NotBlank
    private String user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(String availableTo) {
        this.availableTo = availableTo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
