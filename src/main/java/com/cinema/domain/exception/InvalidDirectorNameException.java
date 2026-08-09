package com.cinema.domain.exception;

public class InvalidDirectorNameException extends RuntimeException {
    public InvalidDirectorNameException(String message) {
        super(message);
    }
}
