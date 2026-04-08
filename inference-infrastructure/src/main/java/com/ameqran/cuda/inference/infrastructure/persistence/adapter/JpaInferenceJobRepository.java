package com.ameqran.cuda.inference.infrastructure.persistence.adapter;

import com.ameqran.cuda.inference.domain.aggregate.InferenceJob;
import com.ameqran.cuda.inference.domain.repository.InferenceJobRepository;
import com.ameqran.cuda.inference.domain.value.JobId;
import com.ameqran.cuda.inference.infrastructure.persistence.mapper.InferenceJobMapper;
import com.ameqran.cuda.inference.infrastructure.persistence.repository.SpringDataInferenceJobRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaInferenceJobRepository implements InferenceJobRepository {

    private final SpringDataInferenceJobRepository springRepository;
    private final InferenceJobMapper mapper;

    public JpaInferenceJobRepository(SpringDataInferenceJobRepository springRepository) {
        this.springRepository = springRepository;
        this.mapper = new InferenceJobMapper();
    }

    @Override
    public void save(InferenceJob job) {
        springRepository.save(mapper.toEntity(job));
    }

    @Override
    public Optional<InferenceJob> findById(JobId jobId) {
        return springRepository.findById(jobId.value()).map(mapper::toDomain);
    }
}
