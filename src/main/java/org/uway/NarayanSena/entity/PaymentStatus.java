package org.uway.NarayanSena.entity;

public enum PaymentStatus {
    CREATED("Created"),
    SUCCESS("Success"),
    FAILED("Failed"),
    PENDING("Pending"),
    CANCELED("Canceled");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static PaymentStatus fromString(String status) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getStatus().equalsIgnoreCase(status)) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + status);
    }
}