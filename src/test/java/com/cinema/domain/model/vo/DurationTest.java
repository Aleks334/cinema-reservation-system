package com.cinema.domain.model.vo;

import com.cinema.domain.exception.InvalidDurationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DurationTest {

    @Test
    void shouldCreateValidDuration() {
        Duration duration = new Duration(2, 30);
        assertEquals(150, duration.totalMinutes());
    }

    @Test
    void shouldCreateDurationFromTotalMinutes() {
        Duration duration = Duration.ofMinutes(163);
        assertEquals(2, duration.hours());
        assertEquals(43, duration.minutes());
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
                () -> new Duration(hours, minutes));
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0, 721})
    void shouldRejectInvalidTotalMinutes(int totalMinutes) {
        assertThrows(InvalidDurationException.class,
                () -> Duration.ofMinutes(totalMinutes));
    }
}
