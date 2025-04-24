package com.learning.grpc.userservice.exception;

public class UnknownTickerException extends RuntimeException {
    private static final String MESSAGE = "Ticker not found";

    public UnknownTickerException() {
        super(MESSAGE);
    }
}
