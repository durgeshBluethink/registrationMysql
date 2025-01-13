package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"referrer", "payments"}) // Exclude lazy-loaded properties
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public boolean isPaymentComplete() {
        return payments != null && payments.stream().anyMatch(payment -> "completed".equalsIgnoreCase(payment.getStatus()));
    }
}
