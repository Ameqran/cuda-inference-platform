package com.ameqran.cuda.inference.domain.aggregate;

import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.value.ModelId;

import java.time.Instant;

public final class Model {

    private final ModelId modelId;
    private final String name;
    private final ModelFormat format;
    private final LayerGraph layers;
    private final Instant registeredAt;

    public Model(ModelId modelId, String name, ModelFormat format, LayerGraph layers, Instant registeredAt) {
        if (modelId == null || format == null || layers == null || registeredAt == null) {
            throw new IllegalArgumentException("Model required fields cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Model name is required");
        }
        this.modelId = modelId;
        this.name = name;
        this.format = format;
        this.layers = layers;
        this.registeredAt = registeredAt;
    }

    public ModelId modelId() {
        return modelId;
    }

    public String name() {
        return name;
    }

    public ModelFormat format() {
        return format;
    }

    public LayerGraph layers() {
        return layers;
    }

    public Instant registeredAt() {
        return registeredAt;
    }
}
