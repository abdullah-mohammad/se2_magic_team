package de.haw.eborrow.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class BorrowDTO {

    @NotBlank
    private long id;

    @NotBlank
    private Date borrowFrom;

    @NotBlank
    private Date borrowTo;

    public BorrowDTO(@NotBlank long id, @NotBlank Date borrowFrom, @NotBlank Date borrowTo) {
        this.id = id;
        this.borrowFrom = borrowFrom;
        this.borrowTo = borrowTo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

}
