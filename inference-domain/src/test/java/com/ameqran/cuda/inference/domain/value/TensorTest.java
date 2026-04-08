package com.ameqran.cuda.inference.domain.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TensorTest {

    @Test
    void shouldRejectShapeDataMismatch() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tensor(new float[]{1f, 2f, 3f}, new int[]{2, 2}, DType.FLOAT32)
        );
    }
}
