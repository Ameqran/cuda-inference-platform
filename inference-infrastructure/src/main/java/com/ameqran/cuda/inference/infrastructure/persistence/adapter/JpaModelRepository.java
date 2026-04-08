package com.ameqran.cuda.inference.infrastructure.persistence.adapter;

import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.infrastructure.persistence.mapper.ModelMapper;
import com.ameqran.cuda.inference.infrastructure.persistence.repository.SpringDataModelRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaModelRepository implements ModelRepository {

    private final SpringDataModelRepository springRepository;
    private final ModelMapper mapper;

    public JpaModelRepository(SpringDataModelRepository springRepository) {
        this.springRepository = springRepository;
        this.mapper = new ModelMapper();
    }

    @Override
    public void save(Model model) {
        springRepository.save(mapper.toEntity(model));
    }

    @Override
    public Optional<Model> findById(ModelId modelId) {
        return springRepository.findById(modelId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Model> findAll(int page, int size) {
        return springRepository.findAll().stream()
                .skip((long) page * size)
                .limit(size)
                .map(mapper::toDomain)
                .toList();
    }
}
