package net.uway.journalApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the Payment entity

    private String orderId;  // Razorpay order ID
    private Double amount;  // Payment amount

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;  // Payment status using enum

    private String paymentId; // Razorpay payment ID (for updating status)
    private String receiptId; // Razorpay receipt ID (for the user)
    private LocalDateTime createdAt; // Timestamp of when the payment was created

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Optionally, you can set the status using a String as well
    public void setStatus(String status) {
        this.status = PaymentStatus.valueOf(status.toUpperCase());
    }

    // Set status using PaymentStatus enum
    public void setStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public String getStatusString() {
        return this.status.getStatus();
    }
}
