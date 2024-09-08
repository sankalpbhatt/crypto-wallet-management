-- Create Table sequence_generator to hold current sequence value based on sequence_type
CREATE TABLE IF NOT EXISTS sequence_generator (
    sequence_type VARCHAR(50) PRIMARY KEY,
    current_value BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
    );

INSERT INTO sequence_generator (sequence_type, current_value) VALUES ('USER', 1);
INSERT INTO sequence_generator (sequence_type, current_value) VALUES ('WALLET', 1);
INSERT INTO sequence_generator (sequence_type, current_value) VALUES ('TRANSACTION', 1);

-- Create Enum TYPE transaction_type if it does not exist
CREATE TYPE IF NOT EXISTS transaction_type AS ENUM ('SEND', 'RECEIVE');
-- Create Enum TYPE transaction_status if it does not exist
CREATE TYPE IF NOT EXISTS transaction_status AS ENUM ('PENDING', 'CONFIRMED', 'FAILED');

-- Create Enum TYPE currency_enum if it does not exist
CREATE TYPE IF NOT EXISTS currency_enum AS ENUM ('BITCOIN', 'ETHEREUM', 'LITECOIN', 'DOGECOIN');

-- Create wallet Table
CREATE TABLE IF NOT EXISTS wallet (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id VARCHAR(25) UNIQUE NOT NULL,
    currency currency_enum NOT NULL,
    public_key VARCHAR(255) NOT NULL,
    encrypted_private_key VARCHAR(255) NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    deleted_at TIMESTAMP
);

-- Create transaction Table
CREATE TABLE IF NOT EXISTS transaction (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id VARCHAR(25) UNIQUE NOT NULL,
    wallet_id UUID FOREIGN KEY REFERENCES Wallet(id),
    type VARCHAR(10),
    amount DECIMAL(18,8),
    status VARCHAR(20),
    transaction_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    deleted_at TIMESTAMP,
    external_reference_id VARCHAR(100) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE

)

-- Create user table
CREATE TABLE IF NOT EXISTS user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(25) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT now(),
    updated_date TIMESTAMP,
    deleted_date TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE CASCADE
);