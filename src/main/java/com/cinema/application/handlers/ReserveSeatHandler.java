package com.cinema.application.handlers;

import com.cinema.application.port.in.command.CommandHandler;
import com.cinema.application.port.in.command.ReserveSeatCommand;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.ticketing.Screening;
import com.google.inject.Inject;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

public class ReserveSeatHandler implements CommandHandler<ReserveSeatCommand> {

    private final ScreeningRepository repository;

    @Inject
    public ReserveSeatHandler(ScreeningRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void handle(ReserveSeatCommand cmd) {
        Objects.requireNonNull(cmd.screeningId());
        Objects.requireNonNull(cmd.screeningSeatId());

        Screening screening = repository.findById(cmd.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + cmd.screeningId() + " not found"
                ));

        screening.reserveSeat(cmd.screeningSeatId());
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
