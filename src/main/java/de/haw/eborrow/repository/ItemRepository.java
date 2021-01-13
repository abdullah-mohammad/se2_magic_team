package de.haw.eborrow.repository;

import de.haw.eborrow.models.Borrow;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import de.haw.eborrow.models.Item;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    List<Item> findByAvailable(boolean available);

    List<Item> findByTitleContaining(String title);

    /*SELECT * FROM ITEM i
    WHERE i.ID NOT IN (
            SELECT b.ITEM_ID   FROM BORROW b
            WHERE '2021-01-28' <= b. BORROW_TO  AND '2021-01-30' >= BORROW_FROM
    )*/

    @Query("SELECT i" +
            " FROM Item i" +
            " WHERE LOWER(i.title) LIKE LOWER(CONCAT('%',:title,'%'))")
    List<Item> filterItemListBy(@Param("title") String title);
}
