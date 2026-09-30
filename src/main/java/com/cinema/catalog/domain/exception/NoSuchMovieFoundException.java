package com.cinema.catalog.domain.exception;

public class NoSuchMovieFoundException extends RuntimeException {
    public NoSuchMovieFoundException(String message) {
        super(message);
    }
}
