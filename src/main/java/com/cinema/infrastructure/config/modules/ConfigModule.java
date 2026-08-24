package com.cinema.infrastructure.config.modules;

import com.cinema.application.port.out.CinemaRepository;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlCinemaRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlMovieRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlScreeningRepository;
import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CinemaRepository.class).to(SqlCinemaRepository.class);
        bind(MovieRepository.class).to(SqlMovieRepository.class);
        bind(ScreeningRepository.class).to(SqlScreeningRepository.class);
    }
}
