package com.cinema.bootstrap.config;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

@Singleton
public final class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    private static final String DEFAULT_ENVIRONMENT = "dev";

    private final Properties properties;
    private final String environment;

    public AppConfig() {
        this.environment = resolveEnvironment();
        this.properties = loadProperties();
        LOGGER.info("Application running in {} mode", environment);
    }

    private String resolveEnvironment() {
        String env = System.getProperty("app.env");
        if (env != null) {
            return env;
        }

        env = System.getenv("APP_ENV");
        if (env != null) {
            return env;
        }

        return DEFAULT_ENVIRONMENT;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        String propertiesFile = "/application-" + environment + ".properties";

        try (InputStream input = AppConfig.class.getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new RuntimeException(
                        "Unable to find configuration file: " + propertiesFile
                );
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application properties", e);
        }

        return props;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getDatabasePath() {
        return Optional.ofNullable(properties.getProperty("db.path")).orElseThrow(() ->
                new IllegalArgumentException("Database path not configured"));
    }

    public boolean isDatabaseAutoInit() {
        return Boolean.parseBoolean(properties.getProperty("db.auto.init", "false"));
    }

    public boolean isSwaggerEnabled() {
        return Boolean.parseBoolean(properties.getProperty("swagger.enabled", "false"));
    }

    public boolean isCorsEnabled() {
        return Boolean.parseBoolean(properties.getProperty("cors.enabled", "false"));
    }

    public Duration getLockTimeout() {
        long minutes = Long.parseLong(properties.getProperty("seat.lock.timeout.minutes", "10"));
        return Duration.ofMinutes(minutes);
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "7070"));
    }
}
