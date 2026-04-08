package com.ameqran.cuda.inference.domain.event;

import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.ModelId;

import java.time.Instant;

public record InferenceJobQueuedEvent(JobId jobId, ModelId modelId, Instant queuedAt) {
}
