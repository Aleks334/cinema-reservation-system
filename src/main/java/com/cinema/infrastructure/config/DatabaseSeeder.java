package com.cinema.infrastructure.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public final class DatabaseSeeder {
    
    private DatabaseSeeder() {
    }
    
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        String dbPath = appConfig.getDatabasePath();

        try(InputStream seedScript = DatabaseConfig.class
                .getResourceAsStream("/db/seed-data.sql")) {

            if (seedScript == null) {
                throw new RuntimeException("Sql with seed data was not found in resources");
            }

            String seedScriptContent = new BufferedReader(
                    new InputStreamReader(seedScript, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                 Statement statement = connection.createStatement()) {

                for (String sql : seedScriptContent.split(";")) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        statement.execute(sql);
                    }
                }
            }

            System.out.println("Database seeded successfully at: " + dbPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to seed database", e);
        }
    }
}