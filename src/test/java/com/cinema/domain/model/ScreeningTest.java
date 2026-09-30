package com.cinema.domain.model;

import com.cinema.ticketing.domain.exception.SeatNotAvailableException;
import com.cinema.ticketing.domain.model.Screening;
import com.cinema.ticketing.domain.model.ScreeningSeat;
import com.cinema.ticketing.domain.model.ScreeningSeatId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import static com.cinema.domain.model.fixtures.ScreeningFixture.*;
import static com.cinema.domain.model.fixtures.ScreeningSeatFixture.anyAvailableSeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreeningTest {

    @Test
    void shouldLockSeatAndMarkAsModified() {
        // given
        ScreeningSeat seat = anyAvailableSeat("B", "10");
        Screening screening = anyScreening(List.of(seat));

        // when
        screening.lockSeat(seat.getId(), TIMEOUT, FIXED_CLOCK);

        // then
        assertThat(screening.getModifiedSeats()).containsExactly(seat);
        assertThat(seat.isLocked()).isTrue();
    }

    @Test
    void shouldReserveSeatAndMarkAsModified() {
        // given
        ScreeningSeat seat = anyAvailableSeat("B", "10");
        Screening screening = anyScreening(List.of(seat));
        screening.lockSeat(seat.getId(), TIMEOUT, FIXED_CLOCK);
        screening.clearModifiedSeats();

        // when
        screening.reserveSeat(seat.getId(), TIMEOUT, FIXED_CLOCK);

        // then
        assertThat(screening.getModifiedSeats()).containsExactly(seat);
        assertThat(seat.isReserved()).isTrue();
    }

    @Test
    void shouldReleaseSeatAndMarkAsModified() {
        // given
        ScreeningSeat seat = anyAvailableSeat("B", "10");
        Screening screening = anyScreening(List.of(seat));
        screening.lockSeat(seat.getId(), TIMEOUT, FIXED_CLOCK);
        screening.clearModifiedSeats();

        // when
        screening.releaseSeat(seat.getId());

        // then
        assertThat(screening.getModifiedSeats()).containsExactly(seat);
        assertThat(seat.isAvailable()).isTrue();
    }

    @Test
    void shouldImplicitlyReleaseAndRelockIfSeatLockWasExpired() {
        // given
        ScreeningSeat seat = anyAvailableSeat("B", "10");
        seat.lock(FIXED_CLOCK);
        Screening screening = anyScreening(List.of(seat));
        Clock expiredTimeClock = Clock.offset(FIXED_CLOCK, Duration.ofMinutes(15));

        // when
        screening.lockSeat(seat.getId(), TIMEOUT, expiredTimeClock);

        // then
        assertThat(seat.isLocked()).isTrue();
        assertThat(seat.getLockedAt()).isEqualTo(Instant.now(expiredTimeClock));
        assertThat(screening.getModifiedSeats()).containsExactly(seat);
    }

    @Test
    void shouldThrowExceptionWhenLockingNonExistentSeat() {
        // given
        Screening screening = anyScreening(List.of());
        ScreeningSeatId nonExistentScreeningSeatId = ScreeningSeatId.generate();

        // when & then
        assertThatThrownBy(() -> screening.lockSeat(nonExistentScreeningSeatId, TIMEOUT, FIXED_CLOCK))
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldCorrectlyFilterAvailableSeats() {
        // given
        ScreeningSeat seat1 = anyAvailableSeat("A", "1");
        ScreeningSeat seat2 = anyAvailableSeat("A", "2");
        Screening screening = anyScreening(List.of(seat1, seat2));
        screening.lockSeat(seat1.getId(), TIMEOUT, FIXED_CLOCK);

        // when
        List<ScreeningSeat> available = screening.getAvailableSeats();

        // then
        assertThat(available).containsExactly(seat2);
        assertThat(available).doesNotContain(seat1);
    }

    @Test
    void shouldHaveEmptyModifiedSeatsListWhenInitiallyCreated() {
        // given
        Screening screening = anyScreening(List.of(anyAvailableSeat("C", "1")));

        // when
        List<ScreeningSeat> modified = screening.getModifiedSeats();

        // then
        assertThat(modified).isEmpty();
    }
}