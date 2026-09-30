package com.cinema.bootstrap.infrastructure.config;

import com.cinema.bootstrap.infrastructure.bus.InMemoryCommandBus;
import com.cinema.bootstrap.infrastructure.bus.InMemoryQueryBus;
import com.cinema.bootstrap.infrastructure.AppConfig;
import com.cinema.bootstrap.infrastructure.DatabaseConfig;
import com.cinema.shared.CommandBus;
import com.cinema.shared.QueryBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.sql.Connection;
import java.time.Clock;

public class BaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandBus.class).to(InMemoryCommandBus.class);
        bind(QueryBus.class).to(InMemoryQueryBus.class);
    }

    @Provides
    private static Clock provideClock() {
        return Clock.systemUTC();
    }

    @Provides
    private static AppConfig provideAppConfig() {
        return new AppConfig();
    }

    @Provides
    private static DatabaseConfig provideDatabaseConfig(AppConfig appConfig) {
        return new DatabaseConfig(appConfig);
    }

    @Provides
    private static Connection provideConnection(DatabaseConfig dbConfig) {
        return dbConfig.getConnection();
    }
}
