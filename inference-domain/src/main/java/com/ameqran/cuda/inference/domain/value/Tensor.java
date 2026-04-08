package com.ameqran.cuda.inference.domain.value;

import java.util.Arrays;

public record Tensor(float[] data, int[] shape, DType dtype) {

    public Tensor {
        if (data == null || shape == null || dtype == null) {
            throw new IllegalArgumentException("Tensor fields cannot be null");
        }

        if (shape.length == 0) {
            throw new IllegalArgumentException("Tensor shape cannot be empty");
        }

        long elements = 1L;
        for (int dim : shape) {
            if (dim <= 0) {
                throw new IllegalArgumentException("Tensor dimensions must be positive");
            }
            elements *= dim;
        }

        if (elements != data.length) {
            throw new IllegalArgumentException("Tensor shape does not match data length");
        }

        data = Arrays.copyOf(data, data.length);
        shape = Arrays.copyOf(shape, shape.length);
    }
}
