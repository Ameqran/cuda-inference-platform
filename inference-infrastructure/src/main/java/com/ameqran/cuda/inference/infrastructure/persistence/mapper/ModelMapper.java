package com.ameqran.cuda.inference.infrastructure.persistence.mapper;

import com.ameqran.cuda.inference.domain.aggregate.Model;
import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;
import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.layer.LayerNode;
import com.ameqran.cuda.inference.domain.value.ModelId;
import com.ameqran.cuda.inference.infrastructure.persistence.entity.ModelEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public final class ModelMapper {

    public ModelEntity toEntity(Model model) {
        ModelEntity entity = new ModelEntity();
        entity.setId(model.modelId().value());
        entity.setName(model.name());
        entity.setFormat(model.format().name());
        entity.setLayerCount(model.layers().size());
        entity.setRegisteredAt(OffsetDateTime.ofInstant(model.registeredAt(), ZoneOffset.UTC));
        return entity;
    }

    public Model toDomain(ModelEntity entity) {
        List<LayerNode> nodes = new ArrayList<>();
        for (int i = 0; i < Math.max(entity.getLayerCount(), 1); i++) {
            nodes.add(new LayerNode("layer-" + i, "Unknown", List.of(), List.of()));
        }

        return new Model(
                new ModelId(entity.getId()),
                entity.getName(),
                ModelFormat.valueOf(entity.getFormat()),
                new LayerGraph(nodes),
                entity.getRegisteredAt().toInstant()
        );
    }
}
