package com.cinema.domain.model;

import com.cinema.domain.model.fixtures.RoomFixture;
import com.cinema.domain.model.fixtures.SeatFixture;
import com.cinema.domain.model.vo.RoomId;
import com.cinema.domain.model.vo.Seat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomTest {

    @Test
    void shouldRejectRoomWithNoSeats() {
        // when & then
        assertThatThrownBy(() ->
                RoomFixture.roomWithSeats(RoomId.generate(), "1", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Room must contain at least one seat");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "A", "Room 1"})
    void shouldRejectInvalidRoomNumber(String roomNumber) {
        // when & then
        assertThatThrownBy(() ->
                RoomFixture.room(RoomId.generate(), roomNumber, 10, 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectRoomWithDuplicatedSeatPositions() {
        // given
        Seat seat1 = SeatFixture.seat("A", "1", SeatType.BASIC);
        Seat seat2 = SeatFixture.seat("A", "1", SeatType.BASIC);

        // when & then
        assertThatThrownBy(() ->
                RoomFixture.roomWithSeats(RoomId.generate(), "1", List.of(seat1, seat2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Room cannot contain duplicate seat positions");
    }
}

