CREATE INDEX IF NOT EXISTS idx_inference_jobs_status ON inference_jobs(status);
CREATE INDEX IF NOT EXISTS idx_inference_jobs_submitted_at ON inference_jobs(submitted_at DESC);
CREATE INDEX IF NOT EXISTS idx_models_registered_at ON models(registered_at DESC);
CREATE INDEX IF NOT EXISTS idx_gpu_devices_free_memory ON gpu_devices(free_memory DESC);
