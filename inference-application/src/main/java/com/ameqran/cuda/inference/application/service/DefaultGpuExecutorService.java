package com.ameqran.cuda.inference.application.service;

import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import com.ameqran.cuda.inference.application.exception.JobNotFoundException;
import com.ameqran.cuda.inference.application.exception.ModelNotFoundException;
import com.ameqran.cuda.inference.domain.aggregate.GpuDevice;
import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.event.InferenceJobCompletedEvent;
import com.ameqran.cuda.inference.domain.event.InferenceJobFailedEvent;
import com.ameqran.cuda.inference.domain.repository.GpuDeviceRepository;
import com.ameqran.cuda.inference.domain.repository.InferenceJobRepository;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.service.InferencePipelineService;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

import java.time.Instant;
import java.util.concurrent.Executor;

public final class DefaultGpuExecutorService implements GpuExecutorService {

    private final Executor executor;
    private final InferenceJobRepository jobRepository;
    private final ModelRepository modelRepository;
    private final GpuDeviceRepository gpuDeviceRepository;
    private final InferencePipelineService pipelineService;
    private final DomainEventPublisher eventPublisher;

    public DefaultGpuExecutorService(Executor executor,
                                     InferenceJobRepository jobRepository,
                                     ModelRepository modelRepository,
                                     GpuDeviceRepository gpuDeviceRepository,
                                     InferencePipelineService pipelineService,
                                     DomainEventPublisher eventPublisher) {
        this.executor = executor;
        this.jobRepository = jobRepository;
        this.modelRepository = modelRepository;
        this.gpuDeviceRepository = gpuDeviceRepository;
        this.pipelineService = pipelineService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void submit(JobId jobId, GpuDevice device) {
        executor.execute(() -> run(jobId, device));
    }

    private void run(JobId jobId, GpuDevice device) {
        InferenceJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + jobId.value()));

        Model model = modelRepository.findById(job.modelId())
                .orElseThrow(() -> new ModelNotFoundException("Model not found: " + job.modelId().value()));

        try {
            job.markRunning();
            TensorPayload result = pipelineService.run(model, job.input(), device);
            job.markCompleted(result);
            jobRepository.save(job);
            gpuDeviceRepository.save(device);
            eventPublisher.publish(new InferenceJobCompletedEvent(jobId, Instant.now()));
        } catch (RuntimeException ex) {
            job.markFailed(ex.getMessage());
            jobRepository.save(job);
            eventPublisher.publish(new InferenceJobFailedEvent(jobId, ex.getMessage(), Instant.now()));
        }
    }
}
