package de.haw.eborrow.repository;

import de.haw.eborrow.models.Borrow;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import de.haw.eborrow.models.Item;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    //List<Item> findByAvailable(boolean available);

    List<Item> findByTitleContaining(String title);

    /*SELECT * FROM ITEM i
    WHERE i.ID NOT IN (
            SELECT b.ITEM_ID   FROM BORROW b
            WHERE '2021-01-28' <= b. BORROW_TO  AND '2021-01-30' >= BORROW_FROM
    )*/

    @Query("SELECT count (i.id)" +
            " FROM Item i " +
            " WHERE i.id = :itemId AND CURRENT_DATE between i.available_from AND i.available_to")
    Long isAvailable(@Param("itemId") long itemId);

    @Query("SELECT i" +
            " FROM Item i" +
            " WHERE LOWER(i.title) LIKE LOWER(CONCAT('%',:title,'%'))")
    List<Item> filterItemListBy(@Param("title") String title);

    @Query("SELECT i FROM Item i" +
            "    WHERE LOWER(i.title) LIKE LOWER(CONCAT('%',:title,'%')) AND i.id NOT IN (" +
            "            SELECT b.item.id  FROM Borrow  b" +
            "            WHERE :fromD <= b.borrowTo AND :tillD >= b.borrowFrom" +
            "    )")
    List<Item> filterItemListBy(@Param("title") String title, @Param("fromD") @Temporal(TemporalType.TIMESTAMP) Date fromD, @Param("tillD") @Temporal(TemporalType.TIMESTAMP) Date tillD);


    @Query("SELECT i FROM Item i" +
            "    WHERE LOWER(i.title) LIKE LOWER(CONCAT('%',:title,'%')) AND i.id NOT IN (" +
            "            SELECT b.item.id  FROM Borrow  b" +
            "            WHERE :fromD >= b.borrowFrom AND :fromD <= b.borrowTo" +
            "    )")
    List<Item> filterItemListBy(@Param("title") String title, @Param("fromD") @Temporal(TemporalType.TIMESTAMP) Date date);

}
