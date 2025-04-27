package com.learning.grpc.userservice.service.advice;

import com.learning.grpc.userservice.exception.InsufficientBalanceException;
import com.learning.grpc.userservice.exception.InsufficientSharesException;
import com.learning.grpc.userservice.exception.UnknownTickerException;
import com.learning.grpc.userservice.exception.UnknownUserException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ServiceExceptionHandler {

    @GrpcExceptionHandler(UnknownTickerException.class)
    public Status handleInvalidArgument(UnknownTickerException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownUser(UnknownUserException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler({InsufficientBalanceException.class, InsufficientSharesException.class})
    public Status handlePreconditionFailure(Exception e) {
        return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
    }
}
