package com.cinema.domain.model;

import com.cinema.catalog.domain.exception.InvalidDurationException;
import com.cinema.catalog.domain.model.MovieDuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieDurationTest {

    @Test
    void shouldCreateValidDuration() {
        MovieDuration movieDuration = new MovieDuration(2, 30);
        assertEquals(150, movieDuration.totalMinutes());
    }

    @Test
    void shouldCreateDurationFromTotalMinutes() {
        MovieDuration movieDuration = MovieDuration.ofMinutes(163);
        assertEquals(2, movieDuration.hours());
        assertEquals(43, movieDuration.minutes());
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 30",
            "2, -1",
            "0, 0",
            "13, 0",
            "0, 60"
    })
    void shouldRejectInvalidDuration(int hours, int minutes) {
        assertThrows(InvalidDurationException.class,
                () -> new MovieDuration(hours, minutes));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0, 721})
    void shouldRejectInvalidTotalMinutes(int totalMinutes) {
        assertThrows(InvalidDurationException.class,
                () -> MovieDuration.ofMinutes(totalMinutes));
    }
}
