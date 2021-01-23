package de.haw.eborrow.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Date borrowFrom;

    private Date borrowTo;

    public Borrow() {
    }

    public Borrow(User user, Item item, Date borrowFrom, Date borrowTo) {
        //this.id = id;
        this.user = user;
        this.item = item;
        this.borrowFrom = borrowFrom;
        this.borrowTo = borrowTo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getBorrowFrom() {
        return borrowFrom;
    }

    public void setBorrowFrom(Date borrowFrom) {
        this.borrowFrom = borrowFrom;
    }

    public Date getBorrowTo() {
        return borrowTo;
    }

    public void setBorrowTo(Date borrowTo) {
        this.borrowTo = borrowTo;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", user=" + user +
                ", item=" + item +
                '}';
    }
}
