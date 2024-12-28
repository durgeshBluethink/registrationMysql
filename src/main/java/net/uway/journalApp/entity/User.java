package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String mobileNumber;
    private String city;
    private String password;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;
}

