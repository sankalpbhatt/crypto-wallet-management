-- Create Table sequence_generator to hold current sequence value based on sequence_type
CREATE TABLE IF NOT EXISTS crypto.sequence_generator (
    sequence_type VARCHAR(50) PRIMARY KEY,
    current_value BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

INSERT INTO crypto.sequence_generator (sequence_type, current_value) VALUES ('USER', 1);
INSERT INTO crypto.sequence_generator (sequence_type, current_value) VALUES ('WALLET', 1);
INSERT INTO crypto.sequence_generator (sequence_type, current_value) VALUES ('TRANSACTION', 1);

-- Create user table
CREATE TABLE IF NOT EXISTS crypto.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(25) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    hash_iteration NUMERIC NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT now(),
    updated_date TIMESTAMP,
    deleted_date TIMESTAMP
);

-- Create wallet Table
CREATE TABLE IF NOT EXISTS crypto.wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id VARCHAR(25) UNIQUE NOT NULL,
    user_id UUID,
    public_key VARCHAR NOT NULL,
    encrypted_private_key VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES crypto.users(id) ON DELETE CASCADE
);

-- Create transaction Table
CREATE TABLE crypto.wallet_balances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL,
    currency VARCHAR(50) NOT NULL,
    balance DECIMAL(18, 8) DEFAULT 0.0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES crypto.wallets(id) ON DELETE CASCADE
);

-- Create transaction Table
CREATE TABLE IF NOT EXISTS crypto.transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id VARCHAR(25) UNIQUE NOT NULL,
    wallet_id UUID,
    type VARCHAR(10),
    amount DECIMAL(18,8),
    status VARCHAR(20),
    transaction_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    external_reference_id VARCHAR(100) UNIQUE,
    FOREIGN KEY (wallet_id) REFERENCES crypto.wallets(id) ON DELETE CASCADE
)