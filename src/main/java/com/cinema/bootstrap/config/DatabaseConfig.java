package com.cinema.bootstrap.config;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    private final AppConfig appConfig;
    private final DataSource dataSource;

    @Inject
    public DatabaseConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.dataSource = createDataSource();

        if (appConfig.isDatabaseAutoInit()) {
            initializeSchema(this.dataSource);
        }
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    private DataSource createDataSource() {
        var config = new SQLiteConfig();
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.enforceForeignKeys(true);

        var ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:" + appConfig.getDatabasePath());

        LOGGER.info("DataSource configured for: {}", appConfig.getDatabasePath());
        return ds;
    }

    private void initializeSchema(DataSource dataSource) {
        try(InputStream schemaStream =
                    this.getClass().getResourceAsStream("/db/schema.sql")) {

            if (schemaStream == null) {
                throw new RuntimeException("Schema file not found in resources");
            }

            String schemaSql = new BufferedReader(
                    new InputStreamReader(schemaStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (var conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
                for (String sql : schemaSql.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql.trim());
                    }
                }
            }

            LOGGER.info("Database schema initialized successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}
