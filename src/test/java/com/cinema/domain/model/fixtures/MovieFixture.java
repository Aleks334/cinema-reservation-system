package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.Movie;
import com.cinema.domain.model.MovieGenre;
import com.cinema.domain.model.vo.Director;
import com.cinema.domain.model.vo.Duration;
import com.cinema.domain.model.vo.MovieId;

public class MovieFixture {

    public static Director director(String firstName, String lastName) {
        return new Director(firstName, lastName);
    }

    public static Duration duration(int hours, int minutes) {
        return new Duration(hours, minutes);
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

