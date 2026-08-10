package com.cinema.application.service;

import com.cinema.application.mapping.ScreeningSeatMapper;
import com.cinema.application.port.in.command.LockSeatCommand;
import com.cinema.application.port.in.command.LockSeatUseCase;
import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Screening;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class LockSeatService implements LockSeatUseCase {

    private final ScreeningRepository repository;
    private final Duration lockTimeout;
    private final Clock clock;

    public LockSeatService(ScreeningRepository repository, Duration lockTimeout, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public ScreeningSeatDto handle(LockSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId());
        Objects.requireNonNull(cmd.screeningSeatId());

        Screening screening = repository.findById(cmd.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + cmd.screeningId() + " not found"
                ));

        screening.lockSeat(cmd.screeningSeatId(), lockTimeout, clock);
        repository.save(screening);
        screening.clearModifiedSeats();

        return screening.getScreeningSeats().stream()
                .filter(s -> s.getId().equals(cmd.screeningSeatId()))
                .map(ScreeningSeatMapper::toDto)
                .findFirst()
                .orElseThrow();
    }
}
