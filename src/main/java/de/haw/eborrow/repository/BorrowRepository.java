package de.haw.eborrow.repository;

import de.haw.eborrow.models.Borrow;
import de.haw.eborrow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowRepository extends JpaRepository<Borrow,Long> {

    @Override
    List<Borrow> findAll();

    List<Borrow> findByUser(Optional<User> user);

    /*@Query("SELECT id FROM borrow WHERE user_id = :userId")
    List<Borrow> getBorrowByUser(@Param("userId") long userId);*/
}
