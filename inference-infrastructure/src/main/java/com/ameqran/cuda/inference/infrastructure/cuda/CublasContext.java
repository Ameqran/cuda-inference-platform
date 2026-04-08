package com.ameqran.cuda.inference.infrastructure.cuda;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class CublasContext implements AutoCloseable {

    public CublasContext() {
    }

    public void cublasSgemm(MemorySegment matrixA,
                            MemorySegment matrixB,
                            MemorySegment matrixC,
                            int m,
                            int n,
                            int k) {
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                float sum = 0f;
                for (int inner = 0; inner < k; inner++) {
                    float a = matrixA.getAtIndex(ValueLayout.JAVA_FLOAT, row * k + inner);
                    float b = matrixB.getAtIndex(ValueLayout.JAVA_FLOAT, inner * n + col);
                    sum += a * b;
                }
                matrixC.setAtIndex(ValueLayout.JAVA_FLOAT, row * n + col, sum);
            }
        }
    }

    @Override
    public void close() {
    }
}
