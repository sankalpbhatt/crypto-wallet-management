DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'crypto') THEN
        CREATE ROLE crypto WITH LOGIN PASSWORD 'crypto@123';
    END IF;
END
$$;

GRANT ALL PRIVILEGES ON DATABASE crypto TO crypto;
