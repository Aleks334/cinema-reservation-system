package com.cinema.domain.exception;

public class LockExpiredException extends RuntimeException {
    public LockExpiredException(String message) {
        super(message);
    }
}
