package com.ameqran.cuda.inference.domain.event;

import com.ameqran.cuda.inference.domain.value.JobId;

import java.time.Instant;

public record InferenceJobCompletedEvent(JobId jobId, Instant completedAt) {
}
