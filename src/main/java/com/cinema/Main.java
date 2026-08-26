package com.cinema;

import com.cinema.infrastructure.config.*;
import com.cinema.infrastructure.config.modules.ApplicationModule;
import com.cinema.infrastructure.config.modules.InfrastructureModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                Modules.requireAtInjectOnConstructorsModule(),
                new InfrastructureModule(),
                new ApplicationModule()
        );

        Application app = injector.getInstance(Application.class);
        app.run();
    }
}
