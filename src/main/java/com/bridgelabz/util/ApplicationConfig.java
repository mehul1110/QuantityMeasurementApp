package com.bridgelabz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private static ApplicationConfig instance;
    private final Properties properties;

    private ApplicationConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Warning: application.properties not found, using defaults");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public String getDbUrl() {
        return properties.getProperty("db.url", "jdbc:h2:./quantitydb");
    }

    public String getUsername() {
        return properties.getProperty("db.username", "sa");
    }

    public String getPassword() {
        return properties.getProperty("db.password", "");
    }

    public int getPoolSize() {
        return Integer.parseInt(properties.getProperty("db.pool.size", "5"));
    }

    public boolean useDatabaseRepository() {
        return "database".equalsIgnoreCase(properties.getProperty("repository.type", "cache"));
    }
}
