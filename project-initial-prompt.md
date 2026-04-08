Build a production-grade GPU inference platform using:
- Java 22, Spring Boot 3.3, PostgreSQL 16, Project Panama FFM API + CUDA 12
- Domain-Driven Design (DDD) — strict layer separation
- Async job-based architecture for GPU workloads

=== DOMAIN LAYER (no Spring, no JPA) ===

Aggregates:
  InferenceJob { JobId, ModelId, TensorPayload input, JobStatus, Instant submittedAt, Instant completedAt, TensorPayload result }
    - status machine: QUEUED → RUNNING → COMPLETED | FAILED
    - method: void markCompleted(TensorPayload result)
    - method: void markFailed(String reason)
  Model { ModelId, String name, ModelFormat(ONNX/TorchScript), LayerGraph layers, Instant registeredAt }
  GpuDevice { DeviceId, int cudaDeviceIndex, long totalMemoryBytes, long freeMemoryBytes }

Value Objects (immutable records):
  Tensor(float[] data, int[] shape, DType dtype)   — validated shape product == data.length
  ModelId(UUID value)
  JobId(UUID value)
  BatchSize(int value)                             — 1..512

Domain Events:
  InferenceJobQueuedEvent, InferenceJobCompletedEvent, InferenceJobFailedEvent, ModelRegisteredEvent

Repository interfaces (domain, no impl):
  InferenceJobRepository, ModelRepository, GpuDeviceRepository

Domain service:
  InferencePipelineService — orchestrates: load weights → allocate GPU memory → run forward pass → collect output

=== APPLICATION LAYER ===

Use Cases:
  SubmitInferenceJobCommand { modelId, float[] inputData, int[] inputShape }
  SubmitInferenceJobUseCase  — validates input tensor shape against model, persists job as QUEUED, publishes InferenceJobQueuedEvent → returns JobId

  GetJobResultQuery { jobId }
  GetJobResultHandler — returns job status + result tensor if COMPLETED

  RegisterModelCommand { name, format, byte[] onnxBytes }
  RegisterModelUseCase  — parses ONNX graph, extracts layer topology, persists Model, uploads weights to S3

Async orchestration:
  JobDispatcher — listens to InferenceJobQueuedEvent, picks available GpuDevice, submits to GpuExecutorService thread pool
  GpuExecutorService — runs InferencePipelineService on a dedicated GPU thread, updates job to COMPLETED/FAILED

=== INFRASTRUCTURE LAYER ===

Panama CUDA bindings:
  CudaRuntime — binds: cudaMalloc, cudaFree, cudaMemcpy, cudaDeviceSynchronize, cudaGetDeviceProperties
  CublasContext — binds: cublasCreate, cublasDestroy, cublasSgemm (matrix multiply for Linear layers)
  CudaKernelLauncher — loads .so of compiled .cu kernels: relu_forward, softmax_forward, layernorm_forward
  All tensors: MemorySegment in SharedArena, managed lifetime via try-with-resources

ONNX model loader (pure Java):
  OnnxModelParser — parses protobuf, extracts: node list, weight initializers, input/output shapes
  WeightUploader — converts float[] weights to native MemorySegment, transfers to GPU via cudaMemcpy
  LayerGraphBuilder — builds domain LayerGraph from ONNX nodes

PostgreSQL persistence (Spring Data JPA):
  inference_jobs: id UUID PK, model_id UUID FK, status VARCHAR, input_shape INT[], result_data BYTEA, submitted_at TIMESTAMPTZ, completed_at TIMESTAMPTZ, error_msg TEXT
  models: id UUID PK, name VARCHAR, format VARCHAR, layer_count INT, registered_at TIMESTAMPTZ
  gpu_devices: id UUID PK, cuda_index INT, total_memory BIGINT
  Flyway: V1__create_models.sql, V2__create_jobs.sql, V3__add_perf_indexes.sql
  JPA entities separate from aggregates — assemblers for mapping

Model registry:
  S3ModelStore — uploads raw ONNX bytes, generates presigned URL for download

=== INTERFACES LAYER ===

REST API (Spring MVC):
  POST   /api/v1/jobs               — SubmitInferenceJobCommand → 202 + { jobId }
  GET    /api/v1/jobs/{id}          — GetJobResultQuery → JobStatusDto (status + result tensor)
  GET    /api/v1/jobs/{id}/stream   — SSE stream: job status changes pushed in real-time
  POST   /api/v1/models             — RegisterModelCommand → 201 + ModelDto
  GET    /api/v1/models             — paginated model list
  GET    /api/v1/devices            — list GPU devices + free memory

=== CROSS-CUTTING ===

- Async: @Async with VirtualThreadTaskExecutor (Project Loom) for job dispatch
- Security: JWT auth, CLIENT and ADMIN roles; rate limiting per API key (Bucket4j)
- Observability: Micrometer — inference_latency_p99, gpu_memory_used_bytes, jobs_queued_gauge
- Retry + circuit breaker: Resilience4j on GPU execution
- Error handling: DomainException → 400, JobNotFoundException → 404, GpuOutOfMemoryException → 503
- Testcontainers: Postgres integration tests; mock CUDA layer for CI (no real GPU needed)
- JMH: benchmark Panama CUDA vs mock (CPU fallback) for batch sizes 1, 32, 128, 512

=== DELIVERABLES ===
Full Maven multi-module project:
  inference-domain / inference-application / inference-infrastructure / inference-interfaces / inference-boot
All source files, CUDA kernel .cu sources, Flyway SQL, docker-compose.yml (Postgres + LocalStack S3), application.yml, OpenAPI spec, and one JMH latency table by batch size.