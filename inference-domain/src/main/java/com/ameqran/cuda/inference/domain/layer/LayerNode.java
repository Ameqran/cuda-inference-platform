package com.ameqran.cuda.inference.domain.layer;

import java.util.List;

public record LayerNode(String name, String opType, List<String> inputs, List<String> outputs) {

    public LayerNode {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("LayerNode name is required");
        }
        if (opType == null || opType.isBlank()) {
            throw new IllegalArgumentException("LayerNode opType is required");
        }
        inputs = inputs == null ? List.of() : List.copyOf(inputs);
        outputs = outputs == null ? List.of() : List.copyOf(outputs);
    }
}
