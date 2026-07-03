create SCHEMA IF NOT EXIST order_platform;

CREATE TABLE IF NOT EXISTS order_platform.customer_transactions (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id VARCHAR(100) NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    currency VARCHAR(7) NOT NULL DEFAULT 'UNKNOWN',

    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


    INDEX idx_customer_id(customer_id),

    INDEX idx_status(status)

);