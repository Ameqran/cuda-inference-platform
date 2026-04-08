package com.ameqran.cuda.inference.application.query;

import com.ameqran.cuda.inference.domain.value.JobId;

public record GetJobResultQuery(JobId jobId) {

    public GetJobResultQuery {
        if (jobId == null) {
            throw new IllegalArgumentException("jobId is required");
        }
    }
}
