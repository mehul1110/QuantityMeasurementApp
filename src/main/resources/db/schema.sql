CREATE TABLE IF NOT EXISTS quantity_measurements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL,
    operand1 VARCHAR(100) NOT NULL,
    operand2 VARCHAR(100),
    result VARCHAR(100),
    has_error BOOLEAN DEFAULT FALSE,
    error_message VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_operation_type ON quantity_measurements(operation_type);
CREATE INDEX IF NOT EXISTS idx_timestamp ON quantity_measurements(timestamp);
