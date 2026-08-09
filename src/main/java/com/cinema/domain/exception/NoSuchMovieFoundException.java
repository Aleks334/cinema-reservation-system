package com.cinema.domain.exception;

public class NoSuchMovieFoundException extends RuntimeException {
    public NoSuchMovieFoundException(String message) {
        super(message);
    }
}
