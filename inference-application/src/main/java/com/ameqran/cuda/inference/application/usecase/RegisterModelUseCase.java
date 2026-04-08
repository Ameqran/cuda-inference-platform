package com.ameqran.cuda.inference.application.usecase;

import com.ameqran.cuda.inference.application.command.RegisterModelCommand;
import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import com.ameqran.cuda.inference.application.port.ModelBinaryStore;
import com.ameqran.cuda.inference.application.port.ModelGraphParser;
import com.ameqran.cuda.inference.application.query.ModelView;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.event.ModelRegisteredEvent;
import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.repository.ModelRepository;
import com.ameqran.cuda.inference.domain.value.ModelId;

import java.time.Instant;

public final class RegisterModelUseCase {

    private final ModelRepository modelRepository;
    private final ModelGraphParser modelGraphParser;
    private final ModelBinaryStore modelBinaryStore;
    private final DomainEventPublisher eventPublisher;

    public RegisterModelUseCase(ModelRepository modelRepository,
                                ModelGraphParser modelGraphParser,
                                ModelBinaryStore modelBinaryStore,
                                DomainEventPublisher eventPublisher) {
        this.modelRepository = modelRepository;
        this.modelGraphParser = modelGraphParser;
        this.modelBinaryStore = modelBinaryStore;
        this.eventPublisher = eventPublisher;
    }

    public ModelView handle(RegisterModelCommand command) {
        ModelId modelId = ModelId.newId();
        LayerGraph layerGraph = modelGraphParser.parse(command.onnxBytes());

        Model model = new Model(modelId, command.name(), command.format(), layerGraph, Instant.now());
        modelRepository.save(model);
        modelBinaryStore.uploadModel(model, command.onnxBytes());
        eventPublisher.publish(new ModelRegisteredEvent(modelId, model.registeredAt()));

        return new ModelView(model.modelId(), model.name(), model.format(), model.layers().size(), model.registeredAt());
    }
}
