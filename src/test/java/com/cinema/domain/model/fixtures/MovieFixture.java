package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.catalog.Movie;
import com.cinema.domain.model.catalog.MovieGenre;
import com.cinema.domain.model.catalog.Director;
import com.cinema.domain.model.catalog.MovieDuration;
import com.cinema.domain.model.catalog.MovieId;

public class MovieFixture {

    public static Director director(String firstName, String lastName) {
        return new Director(firstName, lastName);
    }

    public static MovieDuration duration(int hours, int minutes) {
        return new MovieDuration(hours, minutes);
    }

    public static String validDescription() {
        return "This is a valid movie description long enough to pass validation rules.";
    }

    public static Movie movie(String title) {
        return new Movie(
                MovieId.generate(),
                title,
                director("John", "Doe"),
                validDescription(),
                MovieGenre.ACTION,
                duration(2, 15)
        );
    }
}

