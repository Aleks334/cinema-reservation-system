package com.cinema.bootstrap;

import com.cinema.bootstrap.infrastructure.Application;
import com.cinema.bootstrap.infrastructure.config.BaseModule;
import com.cinema.catalog.infrastructure.config.CatalogModule;
import com.cinema.facility.infrastructure.config.FacilityModule;
import com.cinema.ticketing.infrastructure.config.TicketingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                Modules.requireAtInjectOnConstructorsModule(),
                new BaseModule(),
                new CatalogModule(),
                new FacilityModule(),
                new TicketingModule()
        );

        Application app = injector.getInstance(Application.class);
        app.run();
    }
}
