package com.cinema.application.handler;

import com.cinema.application.port.in.command.CommandHandler;
import com.cinema.application.port.in.command.ReserveSeatCommand;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Screening;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class ReserveSeatHandler implements CommandHandler<ReserveSeatCommand> {

    private final ScreeningRepository repository;
    private final Duration lockTimeout;
    private final Clock clock;

    public ReserveSeatHandler(ScreeningRepository repository, Duration lockTimeout, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public void handle(ReserveSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId());
        Objects.requireNonNull(cmd.screeningSeatId());

        Screening screening = repository.findById(cmd.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + cmd.screeningId() + " not found"
                ));

        screening.reserveSeat(cmd.screeningSeatId(), lockTimeout, clock);
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
