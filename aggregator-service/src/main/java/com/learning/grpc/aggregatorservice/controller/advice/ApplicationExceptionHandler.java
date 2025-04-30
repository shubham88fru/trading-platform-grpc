package com.learning.grpc.aggregatorservice.controller.advice;

import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<String> handleStatusRuntimeException(StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case INVALID_ARGUMENT, FAILED_PRECONDITION ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        };
    }
}
