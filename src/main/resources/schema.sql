CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    password VARCHAR(255),
    email VARCHAR(150) NOT NULL UNIQUE,
    contact_number VARCHAR(10),
    name VARCHAR(50),
    is_blocked BOOLEAN,
    address TEXT,
    is_email_verified BOOLEAN DEFAULT FALSE,
    is_password_created BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_dummy_user BOOLEAN
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE service_category (
    id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(100), -- ENUM-like
    description VARCHAR(255),
    pricing_type VARCHAR(100), -- ENUM-like
    base_price DOUBLE PRECISION CHECK (base_price >= 0.0)
);

CREATE TABLE service_addon (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    additional_price DOUBLE PRECISION CHECK (additional_price >= 0.0),
    service_category_id BIGINT,
    FOREIGN KEY (service_category_id) REFERENCES service_category(id)
);

CREATE TABLE provider_profile (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    services TEXT,
    city VARCHAR(100) NOT NULL,
    hourly_rate DOUBLE PRECISION,
    avg_rating DOUBLE PRECISION,
    status VARCHAR(100), -- ENUM-like
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE provider_schedule (
    id BIGSERIAL PRIMARY KEY,
    provider_id BIGINT,
    day_of_week VARCHAR(15), -- Could also be INTEGER (0=Monday, etc.)
    start_time TIME,
    end_time TIME,
    break_start_time TIME,
    break_end_time TIME,
    FOREIGN KEY (provider_id) REFERENCES provider_profile(id)
);

CREATE TABLE booking (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    provider_id BIGINT,
    service_category_id BIGINT,
    date DATE,
    start_time TIME,
    end_time TIME,
    status VARCHAR(100), -- ENUM-like
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (provider_id) REFERENCES provider_profile(id),
    FOREIGN KEY (service_category_id) REFERENCES service_category(id)
);

CREATE TABLE booking_addon (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT,
    addon_id BIGINT,
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
    FOREIGN KEY (addon_id) REFERENCES service_addon(id)
);

CREATE TABLE rating (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT,
    user_id BIGINT,
    provider_id BIGINT,
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),
    comment VARCHAR(1000),
    created_at TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (provider_id) REFERENCES provider_profile(id)
);

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    expiry TIMESTAMP NOT NULL,
    user_id BIGINT UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE otp (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    code BIGINT NOT NULL CHECK (code BETWEEN 100000 AND 999999),
    expiry TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);