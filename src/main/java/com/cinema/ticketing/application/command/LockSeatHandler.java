package com.cinema.ticketing.application.command;

import com.cinema.shared.CommandHandler;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.cinema.ticketing.domain.exception.ScreeningNotFoundException;
import com.cinema.ticketing.domain.model.Screening;
import com.cinema.bootstrap.infrastructure.AppConfig;
import com.google.inject.Inject;

import java.time.Clock;
import java.util.Objects;

public class LockSeatHandler implements CommandHandler<LockSeatCommand> {

    private final ScreeningRepository repository;
    private final AppConfig appConfig;
    private final Clock clock;

    @Inject
    public LockSeatHandler(ScreeningRepository repository, AppConfig appConfig, Clock clock) {
        this.repository = Objects.requireNonNull(repository);
        this.appConfig = appConfig;
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

        screening.lockSeat(cmd.screeningSeatId(), appConfig.getLockTimeout(), clock);
        repository.save(screening);
        screening.clearModifiedSeats();
    }
}
