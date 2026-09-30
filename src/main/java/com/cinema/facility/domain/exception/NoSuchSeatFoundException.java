package com.cinema.facility.domain.exception;

public class NoSuchSeatFoundException extends RuntimeException {
    public NoSuchSeatFoundException(String message) {
        super(message);
    }
}
