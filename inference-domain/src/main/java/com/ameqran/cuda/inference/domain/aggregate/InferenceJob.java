package com.ameqran.cuda.inference.domain.aggregate;

import com.ameqran.cuda.inference.domain.exception.DomainException;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

import java.time.Instant;
import java.util.Objects;

public final class InferenceJob {

    private final JobId jobId;
    private final ModelId modelId;
    private final TensorPayload input;
    private JobStatus status;
    private final Instant submittedAt;
    private Instant completedAt;
    private TensorPayload result;
    private String failureReason;

    private InferenceJob(JobId jobId, ModelId modelId, TensorPayload input, Instant submittedAt) {
        this.jobId = Objects.requireNonNull(jobId, "jobId is required");
        this.modelId = Objects.requireNonNull(modelId, "modelId is required");
        this.input = Objects.requireNonNull(input, "input is required");
        this.submittedAt = Objects.requireNonNull(submittedAt, "submittedAt is required");
        this.status = JobStatus.QUEUED;
    }

    public static InferenceJob queue(JobId jobId, ModelId modelId, TensorPayload input) {
        return new InferenceJob(jobId, modelId, input, Instant.now());
    }

    public void markRunning() {
        if (status != JobStatus.QUEUED) {
            throw new DomainException("Job can transition to RUNNING only from QUEUED");
        }
        status = JobStatus.RUNNING;
    }

    public void markCompleted(TensorPayload result) {
        if (status != JobStatus.RUNNING) {
            throw new DomainException("Job can transition to COMPLETED only from RUNNING");
        }
        this.status = JobStatus.COMPLETED;
        this.result = Objects.requireNonNull(result, "result is required");
        this.completedAt = Instant.now();
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        if (status.terminal()) {
            throw new DomainException("Terminal jobs cannot transition further");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason is required");
        }
        this.status = JobStatus.FAILED;
        this.completedAt = Instant.now();
        this.failureReason = reason;
    }

    public JobId jobId() {
        return jobId;
    }

    public ModelId modelId() {
        return modelId;
    }

    public TensorPayload input() {
        return input;
    }

    public JobStatus status() {
        return status;
    }

    public Instant submittedAt() {
        return submittedAt;
    }

    public Instant completedAt() {
        return completedAt;
    }

    public TensorPayload result() {
        return result;
    }

    public String failureReason() {
        return failureReason;
    }
}
