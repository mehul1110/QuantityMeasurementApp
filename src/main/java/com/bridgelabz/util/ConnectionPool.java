package com.bridgelabz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private final BlockingQueue<Connection> pool;
    private final int poolSize;

    private ConnectionPool() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        this.poolSize = config.getPoolSize();
        this.pool = new LinkedBlockingQueue<>(poolSize);

        try {
            for (int i = 0; i < poolSize; i++) {
                pool.add(createConnection(config));
            }
            log.info("JDBC Connection Pool initialized with {} connections", poolSize);
        } catch (SQLException e) {
            log.error("Failed to initialize connection pool", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private Connection createConnection(ApplicationConfig config) throws SQLException {
        return DriverManager.getConnection(config.getDbUrl(), config.getUsername(), config.getPassword());
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {
        return pool.take();
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            pool.offer(connection);
        }
    }

    public void shutdown() {
        log.info("Shutting down connection pool...");
        for (Connection conn : pool) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.warn("Error closing connection during shutdown: {}", e.getMessage());
            }
        }
        pool.clear();
    }

    public String getPoolStats() {
        return String.format("Pool Stats: Total=%d, Available=%d", poolSize, pool.size());
    }
}
