package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import net.uway.journalApp.entity.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
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

    private String referrerId;

//    @ManyToOne
//    private User referrer; // User who referred this user

    @ManyToOne
    @JoinColumn(name = "referrer_user_id") // Foreign key column to link referrer
    private User referrer;

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> referredUsers = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;


    public boolean isPaymentComplete() {
        return payments != null && payments.stream()
                .anyMatch(payment -> "completed".equalsIgnoreCase(payment.getStatus()));
    }
}
