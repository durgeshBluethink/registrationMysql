package net.uway.journalApp.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Double amount;


    // Validate the amount field (must be positive)
    public void validateAmount() {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }
    @Override
    public String toString() {
        return "PaymentDto{" +
                "amount=" + amount +
                '}';
    }
}
