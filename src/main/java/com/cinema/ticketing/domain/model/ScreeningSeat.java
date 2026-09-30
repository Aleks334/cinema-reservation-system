package com.cinema.ticketing.domain.model;

import com.cinema.ticketing.domain.exception.SeatAlreadyLockedException;
import com.cinema.ticketing.domain.exception.SeatNotAvailableException;
import com.cinema.facility.domain.model.Seat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class ScreeningSeat {
    private final ScreeningSeatId id;
    private final Seat seat;
    private ScreeningSeatStatus status;
    private Instant lockedAt;
    private int version;

    public ScreeningSeat(
            ScreeningSeatId id,
            Seat seat,
            ScreeningSeatStatus status,
            Instant lockedAt,
            int version
    ) {
        this.id = Objects.requireNonNull(id);
        this.seat = Objects.requireNonNull(seat);
        this.status = Objects.requireNonNull(status);
        this.lockedAt = lockedAt;
        this.version = version;

        validateState();
    }

    public void lock(Clock clock) {
        if (!isAvailable()) {
            if (isReserved()) {
                throw new SeatNotAvailableException("Seat is already reserved");
            }
            throw new SeatAlreadyLockedException("Seat is not available for locking (Current status: " + status + ")");
        }

        this.status = ScreeningSeatStatus.LOCKED;
        this.lockedAt = Instant.now(clock);
    }


    public void reserve(Duration timeout, Clock clock) {
        if (!isLocked()) {
            if (isReserved()) {
                throw new SeatNotAvailableException(
                        "Seat " + seat.getSeatPosition() + " is already reserved"
                );
            }

            throw new SeatNotAvailableException(
                    "Seat " + seat.getSeatPosition() + " must be locked before reservation"
            );
        }

        if (isLockExpired(timeout, clock)) {
            throw new SeatNotAvailableException("Lock for seat " + seat.getSeatPosition() + " has expired");
        }

        this.status = ScreeningSeatStatus.RESERVED;
        this.lockedAt = null;
    }

    public void release() {
        if (!isLocked()) {
            throw new SeatNotAvailableException("Only locked seats can be released");
        }

        this.status = ScreeningSeatStatus.AVAILABLE;
        this.lockedAt = null;
    }

    public boolean isLockExpired(Duration timeout, Clock clock) {
        return isLocked()
                && lockedAt != null
                && Instant.now(clock).isAfter(lockedAt.plus(timeout));
    }

    public ScreeningSeatId getId() {
        return id;
    }

    public Seat getSeat() {
        return seat;
    }

    public ScreeningSeatStatus getStatus() {
        return status;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }

    public int getVersion() {
        return version;
    }
    public void incrementVersion() {
        this.version++;
    }

    public boolean isAvailable() {
        return status == ScreeningSeatStatus.AVAILABLE;
    }
    public boolean isLocked() {
        return status == ScreeningSeatStatus.LOCKED;
    }
    public boolean isReserved() {
        return status == ScreeningSeatStatus.RESERVED;
    }


    private void validateState() {
        if (status == ScreeningSeatStatus.LOCKED && lockedAt == null) {
            throw new IllegalArgumentException("Locked seat must have lockedAt timestamp");
        }
        if (status != ScreeningSeatStatus.LOCKED && lockedAt != null) {
            throw new IllegalArgumentException("Only locked seats can have lockedAt timestamp");
        }
        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScreeningSeat other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
