package com.cinema.domain.model.catalog;

import com.cinema.domain.exception.InvalidDirectorNameException;
import java.util.Objects;

public record Director(String firstName, String lastName) {
    public Director {
        Objects.requireNonNull(firstName, "Director first name cannot be null");
        Objects.requireNonNull(lastName, "Director last name cannot be null");

        if (firstName.isBlank()) {
            throw new InvalidDirectorNameException("Director first name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new InvalidDirectorNameException("Director last name cannot be blank");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
