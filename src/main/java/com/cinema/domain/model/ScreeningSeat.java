package com.cinema.domain.model;

import com.cinema.domain.exception.SeatAlreadyLockedException;
import com.cinema.domain.exception.SeatNotAvailableException;
import com.cinema.domain.model.vo.ScreeningSeatId;
import com.cinema.domain.model.vo.Seat;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public final class ScreeningSeat {
    private final ScreeningSeatId id;
    private final Seat seat;
    private SeatStatus status;
    private Instant lockedAt;
    private int version;

    public ScreeningSeat(
            ScreeningSeatId id,
            Seat seat,
            SeatStatus status,
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

        this.status = SeatStatus.LOCKED;
        this.lockedAt = Instant.now(clock);
    }


    public void reserve(java.time.Duration timeout, Clock clock) {
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

        this.status = SeatStatus.RESERVED;
        this.lockedAt = null;
    }

    public void release() {
        if (!isLocked()) {
            throw new SeatNotAvailableException("Only locked seats can be released");
        }

        this.status = SeatStatus.AVAILABLE;
        this.lockedAt = null;
    }

    public boolean isLockExpired(java.time.Duration timeout, Clock clock) {
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

    public SeatStatus getStatus() {
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
        return status == SeatStatus.AVAILABLE;
    }
    public boolean isLocked() {
        return status == SeatStatus.LOCKED;
    }
    public boolean isReserved() {
        return status == SeatStatus.RESERVED;
    }


    private void validateState() {
        if (status == SeatStatus.LOCKED && lockedAt == null) {
            throw new IllegalArgumentException("Locked seat must have lockedAt timestamp");
        }
        if (status != SeatStatus.LOCKED && lockedAt != null) {
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
