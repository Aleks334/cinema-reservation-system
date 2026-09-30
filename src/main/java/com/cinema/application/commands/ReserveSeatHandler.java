package com.cinema.application.commands;

import com.cinema.application.port.in.CommandHandler;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.ticketing.Screening;
import com.cinema.infrastructure.config.AppConfig;
import com.google.inject.Inject;

import java.time.Clock;
import java.util.Objects;

public class ReserveSeatHandler implements CommandHandler<ReserveSeatCommand> {

    private final ScreeningRepository repository;
    private final AppConfig appConfig;
    private final Clock clock;

    @Inject
    public ReserveSeatHandler(ScreeningRepository repository, AppConfig appConfig, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.appConfig = appConfig;
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

        screening.reserveSeat(cmd.screeningSeatId(), appConfig.getLockTimeout(), clock);
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
