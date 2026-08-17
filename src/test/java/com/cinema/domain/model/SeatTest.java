package com.cinema.domain.model;

import com.cinema.domain.model.facility.Seat;
import com.cinema.domain.model.facility.SeatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.cinema.domain.model.fixtures.SeatFixture.seat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeatTest {

    @Test
    void shouldReturnCorrectSeatPosition() {
        Seat seat = seat("A", "10", SeatType.BASIC);

        assertThat(seat.getSeatPosition()).isEqualTo("A10");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "a", "AA", "1"})
    void shouldRejectInvalidSeatRow(String row) {
        assertThatThrownBy(() ->
                seat(row, "1", SeatType.BASIC))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "0", "-1", "01", "abc"})
    void shouldRejectInvalidSeatNumber(String number) {
        assertThatThrownBy(() ->
                seat("A", number, SeatType.BASIC))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void seatsWithSameStateShouldBeEqual() {
        Seat seat1 = seat("B", "7", SeatType.COMFORT);
        Seat seat2 = seat("B", "7", SeatType.COMFORT);

        assertThat(seat1).isEqualTo(seat2).hasSameHashCodeAs(seat2);
    }

    @Test
    void seatsWithDifferentStateShouldNotBeEqual() {
        Seat seat1 = seat("B", "6", SeatType.COMFORT);
        Seat seat2 = seat("B", "7", SeatType.VIP);

        assertThat(seat1).isNotEqualTo(seat2);
    }
}
