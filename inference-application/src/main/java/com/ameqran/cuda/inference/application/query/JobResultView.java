package com.ameqran.cuda.inference.application.query;

import com.ameqran.cuda.inference.domain.aggregate.JobStatus;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.domain.value.TensorPayload;

public record JobResultView(JobId jobId, JobStatus status, TensorPayload result, String failureReason) {
}
