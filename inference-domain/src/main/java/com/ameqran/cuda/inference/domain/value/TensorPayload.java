package com.ameqran.cuda.inference.domain.value;

public record TensorPayload(Tensor tensor) {

    public TensorPayload {
        if (tensor == null) {
            throw new IllegalArgumentException("TensorPayload tensor cannot be null");
        }
    }
}
