package de.haw.eborrow.models;

import javax.persistence.*;


@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private boolean published;

    public Item(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
        this.published = published;
    }

    public Item() {
    }

    public long getId() {
        return id;
    }

//    public void setId(long id) {
//        this.id = id;
//    }
//
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                '}';
    }
}
