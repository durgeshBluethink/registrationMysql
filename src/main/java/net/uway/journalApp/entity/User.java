package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"referrer", "referredUsers", "payments"}) // Exclude lazy-loaded properties
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;

    private String referralId; // User's own referral code

    private String referrerId; // Referrer's referral code (not userId)

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> referredUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "referrer_user_id") // Foreign key column to link referrer
    private User referrer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    public boolean isPaymentComplete() {
        return payments != null && payments.stream().anyMatch(payment -> "completed".equalsIgnoreCase(payment.getStatus()));
    }
}
