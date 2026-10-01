package com.cinema.ticketing.application.command;

import com.cinema.shared.CommandHandler;
import com.cinema.shared.LockTimeout;
import com.cinema.ticketing.domain.exception.ScreeningNotFoundException;
import com.cinema.ticketing.domain.model.Screening;
import com.cinema.ticketing.domain.model.ScreeningId;
import com.cinema.ticketing.domain.model.ScreeningSeatId;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.google.inject.Inject;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class ReserveSeatHandler implements CommandHandler<ReserveSeatCommand> {

    private final ScreeningRepository repository;
    private final Duration lockTimeout;
    private final Clock clock;

    @Inject
    public ReserveSeatHandler(ScreeningRepository repository, @LockTimeout Duration lockTimeout, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public void handle(ReserveSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId(), "Screening ID cannot be null");
        Objects.requireNonNull(cmd.screeningSeatId(), "Screening Seat ID cannot be null");

        ScreeningId screeningId = ScreeningId.from(cmd.screeningId());
        Screening screening = repository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException("Screening with ID " + cmd.screeningId() + " not found"));

        screening.reserveSeat(ScreeningSeatId.from(cmd.screeningSeatId()), lockTimeout, clock);

        repository.save(screening);
        screening.clearModifiedSeats();
    }
}