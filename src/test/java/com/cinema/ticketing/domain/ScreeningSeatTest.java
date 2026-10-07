package com.cinema.ticketing.domain;

import com.cinema.ticketing.domain.exception.SeatAlreadyLockedException;
import com.cinema.ticketing.domain.exception.SeatNotAvailableException;
import com.cinema.ticketing.domain.model.ScreeningSeat;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static com.cinema.ticketing.domain.fixtures.ScreeningSeatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreeningSeatTest {

    @Test
    void shouldSuccessfullyLockAvailableSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");

        // when
        seat.lock(NOW);

        // then
        assertThat(seat.isLocked()).isTrue();
        assertThat(seat.getLockedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldFailToLockAlreadyLockedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);

        // when & then
        assertThatThrownBy(() -> seat.lock(NOW))
                .isInstanceOf(SeatAlreadyLockedException.class);
    }

    @Test
    void shouldTransitionFromLockedToReservedBeforeTimeout() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);

        // when
        seat.reserve(TIMEOUT, NOW);

        // then
        assertThat(seat.isReserved()).isTrue();
        assertThat(seat.getLockedAt()).isNull();
    }

    @Test
    void shouldRecognizeExpiredLock() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);
        Instant futureTime = NOW.plus(Duration.ofMinutes(11));

        // when
        boolean isLockExpired = seat.isLockExpired(TIMEOUT, futureTime);

        // then
        assertThat(isLockExpired).isTrue();
    }

    @Test
    void shouldFailToReserveIfLockIsExpired() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);
        Instant futureTime = NOW.plus(Duration.ofMinutes(11));

        // when & then
        assertThatThrownBy(() -> seat.reserve(TIMEOUT, futureTime))
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("expired");
    }

    @Test
    void shouldSuccessfullyReleaseLockedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);

        // when
        seat.release();

        // then
        assertThat(seat.isAvailable()).isTrue();
        assertThat(seat.getLockedAt()).isNull();
    }

    @Test
    void shouldFailToReserveSeatDirectlyWithoutLock() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");

        // when & then
        assertThatThrownBy(() -> seat.reserve(TIMEOUT, NOW))
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("must be locked before reservation");
    }

    @Test
    void shouldFailToReleaseReservedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(NOW);
        seat.reserve(TIMEOUT, NOW);

        // when & then
        assertThatThrownBy(seat::release)
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("Only locked seats can be released");
    }
}