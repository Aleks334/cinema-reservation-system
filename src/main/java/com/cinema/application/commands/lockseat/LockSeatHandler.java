package com.cinema.application.commands.lockseat;

import com.cinema.application.mappers.ScreeningSeatMapper;
import com.cinema.application.port.in.LockSeatUseCase;
import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.vo.ScreeningSeatId;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class LockSeatHandler implements LockSeatUseCase {

    private final ScreeningRepository repository;
    private final Duration lockTimeout;
    private final Clock clock;

    public LockSeatHandler(ScreeningRepository repository, Duration lockTimeout, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public ScreeningSeatDto lockSeat(ScreeningId screeningId, ScreeningSeatId seatId) {
        Objects.requireNonNull(screeningId);
        Objects.requireNonNull(seatId);

        Screening screening = repository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        screening.lockSeat(seatId, lockTimeout, clock);
        repository.save(screening);
        screening.clearModifiedSeats();

        return screening.getScreeningSeats().stream()
                .filter(s -> s.getId().equals(seatId))
                .map(ScreeningSeatMapper::toDto)
                .findFirst()
                .orElseThrow();
    }
}
