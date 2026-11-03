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
    land_area_acres DECIMAL(10,2) NOT NULL,
    expected_yield_quintals DECIMAL(10,2) NOT NULL,
    expected_price_per_quintal DECIMAL(12,2) NOT NULL,
    estimated_cost DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_crop_season_farmer
        FOREIGN KEY (farmer_id) REFERENCES farmers(id)
        ON DELETE CASCADE
);