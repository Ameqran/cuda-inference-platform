package com.ameqran.cuda.inference.domain.repository;

import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.value.ModelId;

import java.util.List;
import java.util.Optional;

public interface ModelRepository {

    void save(Model model);

    Optional<Model> findById(ModelId modelId);

    List<Model> findAll(int page, int size);
}
