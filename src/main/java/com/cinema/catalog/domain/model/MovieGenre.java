package com.cinema.catalog.domain.model;

import java.util.Arrays;

public enum MovieGenre {
    ACTION("Action"),
    COMEDY("Comedy"),
    SCI_FI("Science Fiction"),
    THRILLER("Thriller"),
    FANTASY("Fantasy");

    private final String displayName;

    MovieGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MovieGenre fromDisplayName(String value) {
        return Arrays.stream(values())
                .filter(g -> g.displayName.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown genre: " + value));
    }

    @Override
    public String toString() {
        return displayName;
    }
}
