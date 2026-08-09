package com.cinema.domain.exception;

public class NoSuchSeatFoundException extends RuntimeException {
    public NoSuchSeatFoundException(String message) {
        super(message);
    }
}
