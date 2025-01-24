package org.uway.NarayanSena.repository;

import org.uway.NarayanSena.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByReferralId(String referralId);
    boolean existsByReferralId(String referralId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.referrer WHERE u.id = :userId")
    User getUserDetailsWithAllData(long userId);
}