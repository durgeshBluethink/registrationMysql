package org.uway.NarayanSena.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    @JsonIgnore // Exclude password from JSON serialization
    private String password;

    private String referralId;

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> referredUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "referrer_user_id")
    private User referrer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    public boolean isPaymentComplete() {
        return payments != null && !payments.isEmpty() &&
                payments.stream().anyMatch(payment -> payment.getStatus() == PaymentStatus.SUCCESS);
    }
}