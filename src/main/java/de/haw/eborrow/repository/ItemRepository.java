package de.haw.eborrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.haw.eborrow.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByAvailable(boolean available);

    List<Item> findByTitleContaining(String title);

    @Query("SELECT i" +
            " FROM Item i" +
            " WHERE LOWER(i.title) LIKE LOWER(CONCAT('%',:title,'%'))")
    List<Item> filterItemListBy(@Param("title") String title);
}
