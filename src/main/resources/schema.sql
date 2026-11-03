CREATE TABLE IF NOT EXISTS farmers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    village VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crop_seasons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    season_name VARCHAR(30) NOT NULL,
    crop_name VARCHAR(100) NOT NULL,
    land_area_acres DECIMAL(10, 2) NOT NULL,
    expected_yield_quintals DECIMAL(10, 2) NOT NULL,
    expected_price_per_quintal DECIMAL(12, 2) NOT NULL,
    estimated_cost DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_crop_season_farmer
        FOREIGN KEY (farmer_id) REFERENCES farmers(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS loans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmer_id BIGINT NOT NULL,
    loan_type VARCHAR(50) NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    loan_amount DECIMAL(12, 2) NOT NULL,
    annual_interest_rate DECIMAL(5, 2) NOT NULL,
    tenure_months INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loan_farmer
        FOREIGN KEY (farmer_id) REFERENCES farmers(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS repayment_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id BIGINT NOT NULL,
    installment_number INT NOT NULL,
    due_date DATE NOT NULL,
    principal_amount DECIMAL(12, 2) NOT NULL,
    interest_amount DECIMAL(12, 2) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_repayment_schedule_loan
        FOREIGN KEY (loan_id) REFERENCES loans(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_schedule_installment
        UNIQUE (loan_id, installment_number)
);

CREATE TABLE IF NOT EXISTS repayment_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    repayment_schedule_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    amount_paid DECIMAL(12, 2) NOT NULL,
    payment_mode VARCHAR(30) NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_repayment_entry_schedule
        FOREIGN KEY (repayment_schedule_id) REFERENCES repayment_schedules(id)
        ON DELETE CASCADE
);