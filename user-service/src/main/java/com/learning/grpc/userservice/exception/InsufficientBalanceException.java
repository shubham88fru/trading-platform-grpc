package com.learning.grpc.userservice.exception;

public class InsufficientBalanceException extends RuntimeException {
    private static final String MESSAGE = "User [id=%d] does not have enough fund to complete the transaction";

    public InsufficientBalanceException(int userId) {
        super(MESSAGE.formatted(userId));
    }
}
