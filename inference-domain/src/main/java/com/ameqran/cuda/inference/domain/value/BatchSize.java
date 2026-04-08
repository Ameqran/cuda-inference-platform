package com.ameqran.cuda.inference.domain.value;

public record BatchSize(int value) {

    public BatchSize {
        if (value < 1 || value > 512) {
            throw new IllegalArgumentException("BatchSize must be within [1, 512]");
        }
    }
}
