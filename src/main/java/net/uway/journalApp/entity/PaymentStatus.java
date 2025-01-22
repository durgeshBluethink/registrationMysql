package net.uway.journalApp.entity;

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
}
