package com.cinema.domain.model;

import com.cinema.domain.exception.SeatAlreadyLockedException;
import com.cinema.domain.exception.SeatNotAvailableException;
import com.cinema.domain.model.ticketing.ScreeningSeat;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import static com.cinema.domain.model.fixtures.ScreeningSeatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreeningSeatTest {

    @Test
    void shouldSuccessfullyLockAvailableSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");

        // when
        seat.lock(FIXED_CLOCK);

        // then
        assertThat(seat.isLocked()).isTrue();
        assertThat(seat.getLockedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldFailToLockAlreadyLockedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);

        // when & then
        assertThatThrownBy(() -> seat.lock(FIXED_CLOCK))
                .isInstanceOf(SeatAlreadyLockedException.class);
    }

    @Test
    void shouldTransitionFromLockedToReservedBeforeTimeout() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);

        // when
        seat.reserve(TIMEOUT, FIXED_CLOCK);

        // Then
        assertThat(seat.isReserved()).isTrue();
        assertThat(seat.getLockedAt()).isNull();
    }

    @Test
    void shouldRecognizeExpiredLock() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);
        Clock futureClock = Clock.offset(FIXED_CLOCK, Duration.ofMinutes(11));

        // when
        boolean isLockExpired = seat.isLockExpired(TIMEOUT, futureClock);

        // then
        assertThat(isLockExpired).isTrue();
    }

    @Test
    void shouldFailToReserveIfLockIsExpired() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);
        Clock futureClock = Clock.offset(FIXED_CLOCK, Duration.ofMinutes(11));

        // when & then
        assertThatThrownBy(() -> seat.reserve(TIMEOUT, futureClock))
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("expired");
    }

    @Test
    void shouldSuccessfullyReleaseLockedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);

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
        assertThatThrownBy(() -> seat.reserve(TIMEOUT, FIXED_CLOCK))
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("must be locked before reservation");
    }

    @Test
    void shouldFailToReleaseReservedSeat() {
        // given
        ScreeningSeat seat = anyAvailableSeat("A", "1");
        seat.lock(FIXED_CLOCK);
        seat.reserve(TIMEOUT, FIXED_CLOCK);

        // when & then
        assertThatThrownBy(() -> seat.release())
                .isInstanceOf(SeatNotAvailableException.class)
                .hasMessageContaining("Only locked seats can be released");
    }
}