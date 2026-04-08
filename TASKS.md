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
- [x] Add commands and queries.
- [x] Implement submit job, get result, register model use cases.
- [x] Add async orchestration ports (`JobDispatcher`, `GpuExecutorService`).

## 4. Infrastructure Layer
- [x] Add Panama CUDA interop stubs (`CudaRuntime`, `CublasContext`, `CudaKernelLauncher`).
- [x] Add ONNX parser + graph builder + weight uploader components.
- [x] Add persistence entities, assemblers, Spring Data repositories.
- [x] Add Flyway SQL migrations.
- [x] Add S3 model store and docker compose stack (Postgres + LocalStack).

## 5. Interfaces + Boot Layer
- [x] Add REST endpoints for jobs/models/devices.
- [x] Add SSE endpoint for job status updates.
- [x] Add exception handlers and DTO mappings.
- [x] Add security (JWT roles) and API key rate limiting (Bucket4j).
- [x] Add observability metrics and resilience wrappers.
- [x] Add `application.yml` and OpenAPI spec.

## 6. Testing + Benchmarking
- [x] Add Testcontainers-based PostgreSQL integration tests.
- [x] Add mock CUDA strategy for CI.
- [x] Add JMH benchmark and latency table by batch size.





