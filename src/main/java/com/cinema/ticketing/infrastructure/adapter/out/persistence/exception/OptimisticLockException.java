package com.cinema.ticketing.infrastructure.adapter.out.persistence.exception;

public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {
        super(message);
    }
}
