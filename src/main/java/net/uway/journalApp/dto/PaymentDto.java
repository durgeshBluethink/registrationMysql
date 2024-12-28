package net.uway.journalApp.dto;

public class PaymentDto {
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentDto{" +
                "amount=" + amount +
                '}';
    }
}
