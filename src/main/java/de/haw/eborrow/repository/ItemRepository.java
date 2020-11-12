package de.haw.eborrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.haw.eborrow.models.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByAvailable(boolean available);

    List<Item> findByTitleContaining(String title);
}
