package com.cinema.domain.model;

import com.cinema.domain.model.vo.MovieId;
import org.junit.jupiter.api.Test;

import static com.cinema.domain.model.fixtures.MovieFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovieTest {

    @Test
    void shouldRejectBlankTitle() {
        assertThatThrownBy(() -> movie(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Movie title cannot be blank");
    }

    @Test
    void shouldRejectTooShortDescription() {
        String toShortDescription = "Too short";

        assertThatThrownBy(() ->
                new Movie(
                        MovieId.generate(),
                        "MovieTitle",
                        director("John", "Doe"),
                        toShortDescription,
                        MovieGenre.ACTION,
                        duration(2, 15)
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Movie description must be between 50 and 300 characters");
    }

    @Test
    void shouldRejectTooLongDescription() {
        String tooLongDescription = "A".repeat(301);

        assertThatThrownBy(() ->
                new Movie(
                        MovieId.generate(),
                        "MovieTitle",
                        director("John", "Doe"),
                        tooLongDescription,
                        MovieGenre.ACTION,
                        duration(2, 15)
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Movie description must be between 50 and 300 characters");
    }
}
