package com.cinema.application.handler;

import com.cinema.application.port.in.command.CommandHandler;
import com.cinema.application.port.in.command.LockSeatCommand;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Screening;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class LockSeatHandler implements CommandHandler<LockSeatCommand> {

    private final ScreeningRepository repository;
    private final Duration lockTimeout;
    private final Clock clock;

    public LockSeatHandler(ScreeningRepository repository, Duration lockTimeout, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public void handle(LockSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId());
        Objects.requireNonNull(cmd.screeningSeatId());

        Screening screening = repository.findById(cmd.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + cmd.screeningId() + " not found"
                ));

        screening.lockSeat(cmd.screeningSeatId(), lockTimeout, clock);
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
