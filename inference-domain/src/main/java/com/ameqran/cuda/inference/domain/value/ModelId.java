package com.ameqran.cuda.inference.domain.value;

import java.util.UUID;

public record ModelId(UUID value) {

    public ModelId {
        if (value == null) {
            throw new IllegalArgumentException("ModelId cannot be null");
        }
    }

    public static ModelId newId() {
        return new ModelId(UUID.randomUUID());
    }
}
