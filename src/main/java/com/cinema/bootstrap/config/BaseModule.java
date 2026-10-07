package com.cinema.bootstrap.config;

import com.cinema.bootstrap.config.bus.InMemoryCommandBus;
import com.cinema.bootstrap.config.bus.InMemoryQueryBus;
import com.cinema.bootstrap.config.web.GlobalExceptionHandlerMapper;
import com.cinema.shared.CommandBus;
import com.cinema.shared.ExceptionHandlerMapper;
import com.cinema.shared.LockTimeout;
import com.cinema.shared.QueryBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import java.sql.Connection;
import java.time.Clock;
import java.time.Duration;

public class BaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandBus.class).to(InMemoryCommandBus.class);
        bind(QueryBus.class).to(InMemoryQueryBus.class);

        bind(Application.class).in(Singleton.class);
        bind(AppConfig.class).in(Singleton.class);
        bind(DatabaseConfig.class).in(Singleton.class);

        Multibinder<ExceptionHandlerMapper> exceptionBinder = Multibinder.newSetBinder(binder(), ExceptionHandlerMapper.class);
        exceptionBinder.addBinding().toInstance(new GlobalExceptionHandlerMapper());
    }

    @Provides
    private static Clock provideClock() {
        return Clock.systemUTC();
    }

    @Provides @LockTimeout
    private static Duration provideLockTimeout(AppConfig appConfig) {
        return appConfig.getLockTimeout();
    }

    @Provides
    private static Connection provideConnection(DatabaseConfig dbConfig) {
        return dbConfig.getConnection();
    }
}
