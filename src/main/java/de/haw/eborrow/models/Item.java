package de.haw.eborrow.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Column(length = 5000)
    private String description;

    private Date available_from;

    private Date available_to;

    private String picture;

    @ManyToOne
    //@JsonBackReference("user")
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy="user")
    private List<Borrow> borrowedItems = new ArrayList<>();

    public Item(String title, String description, String picture, Date availableFrom, Date availableTo) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.available_from = availableFrom;
        this.available_to = availableTo;;
    }

    public Item(String title, String description, String picture, Date availableFrom, Date availableTo, User user) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.available_from = availableFrom;
        this.available_to = availableTo;
        this.user = user;
    }

    public Item() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Date getAvailableFrom() {
        return available_from;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.available_from = availableFrom;
    }

    public Date getAvailableTo() {
        return available_to;
    }

    public void setAvailableTo(Date availableTo) {
        this.available_to = availableTo;
    }

    public void addItemToBorrowedItems(Borrow borrow){
        this.borrowedItems.add(borrow);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", availableFrom=" + available_from +
                ", availableTo=" + available_to +
                ", user=" + user +
                '}';
    }
}
