# Task List

## 1. Bootstrap
- [x] Create Maven multi-module skeleton and parent dependency management.
- [x] Add module-level `pom.xml` files and baseline folder layout.
- [x] Add repository baseline docs (`README`, `.gitignore`).

## 2. Domain Layer
- [x] Implement aggregates (`InferenceJob`, `Model`, `GpuDevice`).
- [x] Implement value objects and validation (`Tensor`, IDs, `BatchSize`).
- [x] Add domain events and repository interfaces.
- [x] Add domain service contract (`InferencePipelineService`).
- [x] Add domain tests for state machine and tensor shape validation.

## 3. Application Layer
- [ ] Add commands and queries.
- [ ] Implement submit job, get result, register model use cases.
- [ ] Add async orchestration ports (`JobDispatcher`, `GpuExecutorService`).

## 4. Infrastructure Layer
- [ ] Add Panama CUDA interop stubs (`CudaRuntime`, `CublasContext`, `CudaKernelLauncher`).
- [ ] Add ONNX parser + graph builder + weight uploader components.
- [ ] Add persistence entities, assemblers, Spring Data repositories.
- [ ] Add Flyway SQL migrations.
- [ ] Add S3 model store and docker compose stack (Postgres + LocalStack).

## 5. Interfaces + Boot Layer
- [ ] Add REST endpoints for jobs/models/devices.
- [ ] Add SSE endpoint for job status updates.
- [ ] Add exception handlers and DTO mappings.
- [ ] Add security (JWT roles) and API key rate limiting (Bucket4j).
- [ ] Add observability metrics and resilience wrappers.
- [ ] Add `application.yml` and OpenAPI spec.

## 6. Testing + Benchmarking
- [ ] Add Testcontainers-based PostgreSQL integration tests.
- [ ] Add mock CUDA strategy for CI.
- [ ] Add JMH benchmark and latency table by batch size.

