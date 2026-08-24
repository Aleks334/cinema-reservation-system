package com.cinema.application.handlers;

import com.cinema.application.port.in.command.CommandHandler;
import com.cinema.application.port.in.command.LockSeatCommand;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.ticketing.Screening;
import com.google.inject.Inject;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class LockSeatHandler implements CommandHandler<LockSeatCommand> {

    private final ScreeningRepository repository;

    @Inject
    public LockSeatHandler(ScreeningRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void handle(LockSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId());
        Objects.requireNonNull(cmd.screeningSeatId());

        Screening screening = repository.findById(cmd.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + cmd.screeningId() + " not found"
                ));

        screening.lockSeat(cmd.screeningSeatId());
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
