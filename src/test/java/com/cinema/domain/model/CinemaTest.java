package com.cinema.domain.model;

import com.cinema.domain.model.vo.CinemaId;
import com.cinema.domain.model.vo.RoomId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cinema.domain.model.fixtures.RoomFixture.room;
import static com.cinema.domain.model.fixtures.CinemaFixture.cinema;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CinemaTest {

    @Test
    void shouldReturnTotalCinemaCapacityAsSumOfRoomsCapacity() {
        // given
        Room room1 = room(RoomId.generate(), "1", 10, 16);
        Room room2 = room(RoomId.generate(), "2", 20, 16);
        Cinema cinema = cinema(CinemaId.generate(), "MyCinema", List.of(room1, room2));

        // when
        int capacity = cinema.getTotalCapacity();

        // then
        assertThat(capacity).isEqualTo(10 * 16 + 20 * 16);
    }

    @Test
    void shouldRejectWhenCinemaContainsDuplicateRoomNumbers() {
        // given
        Room room1 = room(RoomId.generate(), "1", 10, 20);
        Room room2 = room(RoomId.generate(), "1", 10, 20);

        // when & then
        assertThatThrownBy(() ->
               cinema(CinemaId.generate(), "MyCinema", List.of(room1, room2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cinema cannot contain rooms with duplicate numbers");
    }
}