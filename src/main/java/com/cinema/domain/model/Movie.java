package com.cinema.domain.model;

import com.cinema.domain.model.vo.Director;
import com.cinema.domain.model.vo.Duration;
import com.cinema.domain.model.vo.MovieId;

import java.util.Objects;

public final class Movie {
    private final MovieId id;
    private final String title;
    private final Director director;
    private final String description;
    private final MovieGenre genre;
    private final Duration duration;

    private static final int MIN_DESCRIPTION_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 300;

    public Movie(MovieId id, String title, Director director, String description,
                 MovieGenre genre, Duration duration) {
        this.id = Objects.requireNonNull(id, "Movie ID cannot be null");

        this.title = Objects.requireNonNull(title, "Movie title cannot be null");
        if(title.isBlank()) {
            throw new IllegalArgumentException("Movie title cannot be blank");
        }

        this.director = Objects.requireNonNull(director, "Movie director cannot be null");

        this.description = Objects.requireNonNull(description, "Movie description cannot be null");
        if(description.length() < MIN_DESCRIPTION_LENGTH ||
           description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Movie description must be between %d and %d characters",
                              MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH));
        }

        this.genre = Objects.requireNonNull(genre, "Movie genre cannot be null");
        this.duration = Objects.requireNonNull(duration, "Movie duration cannot be null");
    }

    public MovieId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Director getDirector() {
        return director;
    }

    public String getDescription() {
        return description;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
