package com.ameqran.cuda.inference.domain.repository;

import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.value.JobId;

import java.util.Optional;

public interface InferenceJobRepository {

    void save(InferenceJob job);

    Optional<InferenceJob> findById(JobId jobId);
}
