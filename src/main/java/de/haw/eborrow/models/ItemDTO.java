package de.haw.eborrow.models;

import javax.validation.constraints.NotBlank;

public class ItemDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String available;

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

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
