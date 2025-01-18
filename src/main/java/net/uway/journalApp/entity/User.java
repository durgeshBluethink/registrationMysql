package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import net.uway.journalApp.entity.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"referrer", "payments"}) // Exclude lazy-loaded properties
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id;

    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;
    private String referralId;

    @ManyToOne
    private User referrer; // User who referred this user

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL)
    private List<User> referredUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public boolean isPaymentComplete() {
        return payments != null && payments.stream().anyMatch(payment -> "completed".equalsIgnoreCase(payment.getStatus()));
    }
}
