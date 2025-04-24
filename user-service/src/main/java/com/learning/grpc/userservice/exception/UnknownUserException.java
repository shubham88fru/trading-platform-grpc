package com.learning.grpc.userservice.exception;

public class UnknownUserException extends RuntimeException {
    private static final String MESSAGE = "User [id=%d] not found";

    public  UnknownUserException(int userId) {
        super(MESSAGE.formatted(userId));
    }

}
