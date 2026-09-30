package com.cinema.bootstrap.infrastructure;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Singleton
public class DatabaseConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    private static Connection connection;
    private final AppConfig appConfig;

    @Inject
    public DatabaseConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConfig.class) {
                if (connection == null) {
                    connection = initializeConnection();
                }
            }
        }
        return connection;
    }

    private Connection initializeConnection() {
        try {
            String url = "jdbc:sqlite:" + appConfig.getDatabasePath();
            Connection conn = DriverManager.getConnection(url);

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
                stmt.execute("PRAGMA foreign_keys=ON");
            }

            LOGGER.info("Database connection established: {}", appConfig.getDatabasePath());

            if (appConfig.isDatabaseAutoInit()) {
                initializeSchema(conn);
            }

            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    private void initializeSchema(Connection conn) {
        try {
            InputStream schemaStream = DatabaseConfig.class
                    .getResourceAsStream("/db/schema.sql");

            if (schemaStream == null) {
                throw new RuntimeException("Schema file not found in resources");
            }

            String schemaSql = new BufferedReader(
                    new InputStreamReader(schemaStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Statement stmt = conn.createStatement()) {
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
