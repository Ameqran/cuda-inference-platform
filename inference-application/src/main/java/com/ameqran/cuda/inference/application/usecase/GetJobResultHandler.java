package com.ameqran.cuda.inference.application.usecase;

import com.ameqran.cuda.inference.application.exception.JobNotFoundException;
import com.ameqran.cuda.inference.application.query.GetJobResultQuery;
import com.ameqran.cuda.inference.application.query.JobResultView;
import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.repository.InferenceJobRepository;

public final class GetJobResultHandler {

    private final InferenceJobRepository jobRepository;

    public GetJobResultHandler(InferenceJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResultView handle(GetJobResultQuery query) {
        InferenceJob job = jobRepository.findById(query.jobId())
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + query.jobId().value()));

        return new JobResultView(job.jobId(), job.status(), job.result(), job.failureReason());
    }
}
