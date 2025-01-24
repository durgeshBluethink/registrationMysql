package org.uway.NarayanSena.exception;

public class PaymentCreationFailedException extends RuntimeException {
    public PaymentCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}