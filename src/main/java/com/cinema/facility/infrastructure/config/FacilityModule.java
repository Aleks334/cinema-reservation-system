package com.cinema.facility.infrastructure.config;

import com.cinema.facility.domain.port.CinemaRepository;
import com.cinema.facility.infrastructure.adapter.out.persistence.SqlCinemaRepository;
import com.google.inject.AbstractModule;

public class FacilityModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CinemaRepository.class).to(SqlCinemaRepository.class);
    }
}
