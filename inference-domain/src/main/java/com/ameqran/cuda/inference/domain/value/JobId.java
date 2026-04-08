package com.ameqran.cuda.inference.domain.value;

import java.util.UUID;

public record JobId(UUID value) {

    public JobId {
        if (value == null) {
            throw new IllegalArgumentException("JobId cannot be null");
        }
    }

    public static JobId newId() {
        return new JobId(UUID.randomUUID());
    }
}
