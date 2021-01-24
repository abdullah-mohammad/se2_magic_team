package de.haw.eborrow.repository;

import de.haw.eborrow.models.Borrow;
import de.haw.eborrow.models.Item;
import de.haw.eborrow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow,Long> {

    @Override
    List<Borrow> findAll();

    List<Borrow> findByUser(User user);

    List<Borrow> findByItem(Item item);

    /*@Query("SELECT id FROM borrow WHERE user_id = :userId")
    List<Borrow> getBorrowByUser(@Param("userId") long userId);*/

    @Query("SELECT count (b.item.id)" +
            " FROM Borrow b " +
            " WHERE b.item.id = :itemId AND :availableFromD <= b.borrowTo AND :availableToD >= b.borrowFrom")
    int getBorrowedItemIn(@Param("itemId") long itemId, @Param("availableFromD") @Temporal(TemporalType.TIMESTAMP) Date availableFromD, @Param("availableToD") @Temporal(TemporalType.TIMESTAMP) Date availableToD);

}
