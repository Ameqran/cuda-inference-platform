package com.ameqran.cuda.inference.application.usecase;

import com.ameqran.cuda.inference.application.query.ModelView;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;

import java.util.List;

public final class ListModelsUseCase {

    private final ModelRepository modelRepository;

    public ListModelsUseCase(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<ModelView> handle(int page, int size) {
        return modelRepository.findAll(page, size).stream()
                .map(this::toView)
                .toList();
    }

    private ModelView toView(Model model) {
        return new ModelView(model.modelId(), model.name(), model.format(), model.layers().size(), model.registeredAt());
    }
}
