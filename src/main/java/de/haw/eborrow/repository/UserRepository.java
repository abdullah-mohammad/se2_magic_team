package de.haw.eborrow.repository;


import de.haw.eborrow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Query("SELECT password FROM User WHERE id = :userId")
    String getUserPass(@Param("userId") long id);
}