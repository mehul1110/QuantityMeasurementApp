package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.exception.DatabaseException;
import com.bridgelabz.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {
    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);
    private final ConnectionPool pool;

    public QuantityMeasurementDatabaseRepository() {
        this.pool = ConnectionPool.getInstance();
        initializeDatabase();
    }

    private void initializeDatabase() {
        log.info("Checking database initialization...");
        Connection conn = null;
        try {
            conn = pool.getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "QUANTITY_MEASUREMENTS", null)) {
                if (!rs.next()) {
                    log.info("Table 'QUANTITY_MEASUREMENTS' not found. Executing schema.sql...");
                    executeSchemaSql(conn);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize database", e);
            throw new DatabaseException("Database initialization failed", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
    }

    private void executeSchemaSql(Connection conn) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (is == null) throw new DatabaseException("db/schema.sql not found");
            String sql = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));
            
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(statement);
                    }
                }
            }
            log.info("Database schema applied successfully");
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        String sql = "INSERT INTO quantity_measurements (operation_type, operand1, operand2, result, has_error, error_message, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, entity.getOperationType());
                pstmt.setString(2, entity.getOperand1());
                pstmt.setString(3, entity.getOperand2());
                pstmt.setString(4, entity.getResult());
                pstmt.setBoolean(5, entity.isHasError());
                pstmt.setString(6, entity.getErrorMessage());
                pstmt.setTimestamp(7, Timestamp.valueOf(entity.getTimestamp()));
                pstmt.executeUpdate();
                log.info("Saved measurement to database: {}", entity.getOperationType());
            }
        } catch (Exception e) {
            log.error("Error saving entity to database: {}", e.getMessage(), e);
            throw new DatabaseException("Could not save to database", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        String sql = "SELECT * FROM quantity_measurements ORDER BY timestamp DESC";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    measurements.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            log.error("Error retrieving measurements from database", e);
            throw new DatabaseException("Could not fetch from database", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
        return measurements;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM quantity_measurements";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (Exception e) {
            throw new DatabaseException("Count failed", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
        return 0;
    }

    public void deleteAll() {
        String sql = "DELETE FROM quantity_measurements";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                log.warn("All measurements deleted from database");
            }
        } catch (Exception e) {
            throw new DatabaseException("Delete all failed", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
    }

    public List<QuantityMeasurementEntity> findByOperationType(String type) {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        String sql = "SELECT * FROM quantity_measurements WHERE operation_type = ? ORDER BY timestamp DESC";
        Connection conn = null;
        try {
            conn = pool.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, type);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        measurements.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DatabaseException("Find by type failed", e);
        } finally {
            if (conn != null) pool.releaseConnection(conn);
        }
        return measurements;
    }

    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        String opType = rs.getString("operation_type");
        String op1 = rs.getString("operand1");
        String op2 = rs.getString("operand2");
        String res = rs.getString("result");
        boolean hasErr = rs.getBoolean("has_error");
        String errMsg = rs.getString("error_message");
        LocalDateTime ts = rs.getTimestamp("timestamp").toLocalDateTime();

        QuantityMeasurementEntity entity;
        if (hasErr) {
            entity = new QuantityMeasurementEntity(opType, op1, op2, errMsg, true);
        } else if (op2 == null) {
            entity = new QuantityMeasurementEntity(opType, op1, res);
        } else {
            entity = new QuantityMeasurementEntity(opType, op1, op2, res);
        }
        
        entity.setTimestamp(ts);
        return entity;
    }

    public void close() {
        pool.shutdown();
    }
}
