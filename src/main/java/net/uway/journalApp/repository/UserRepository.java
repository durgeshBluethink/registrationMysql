package net.uway.journalApp.repository;

import net.uway.journalApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralId(String referralId);
    boolean existsByReferralId(String referralId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.referrer LEFT JOIN FETCH u.payments WHERE u.id = :userId")
    User getUserDetailsWithAllData(UUID userId);
}
