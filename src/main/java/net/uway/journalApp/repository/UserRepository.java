package net.uway.journalApp.repository;

import net.uway.journalApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.referrer LEFT JOIN FETCH u.payments WHERE u.id = :userId")
    User getUserDetailsWithAllData(Long userId);
}
