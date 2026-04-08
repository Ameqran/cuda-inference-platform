CREATE TABLE IF NOT EXISTS inference_jobs (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL REFERENCES models(id),
    status VARCHAR(32) NOT NULL,
    input_shape INT[] NOT NULL,
    input_data BYTEA,
    result_data BYTEA,
    submitted_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    error_msg TEXT
);

CREATE TABLE IF NOT EXISTS gpu_devices (
    id UUID PRIMARY KEY,
    cuda_index INT NOT NULL,
    total_memory BIGINT NOT NULL,
    free_memory BIGINT NOT NULL
);
