CREATE TABLE IF NOT EXISTS models (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    format VARCHAR(32) NOT NULL,
    layer_count INT NOT NULL,
    registered_at TIMESTAMPTZ NOT NULL,
    s3_key VARCHAR(512)
);
