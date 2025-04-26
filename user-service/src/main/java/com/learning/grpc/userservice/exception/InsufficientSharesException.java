package com.learning.grpc.userservice.exception;

public class InsufficientSharesException extends RuntimeException {
    private static final String MESSAGE = "User [id=%d] does not have enough shares to complete the transaction";

    public InsufficientSharesException(int userId) {
        super(MESSAGE.formatted(userId));
    }
}
