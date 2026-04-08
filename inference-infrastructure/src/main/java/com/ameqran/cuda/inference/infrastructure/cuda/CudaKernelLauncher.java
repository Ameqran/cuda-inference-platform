package com.ameqran.cuda.inference.infrastructure.cuda;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class CudaKernelLauncher {

    public void reluForward(MemorySegment input, MemorySegment output, int elementCount) {
        for (int index = 0; index < elementCount; index++) {
            float value = input.getAtIndex(ValueLayout.JAVA_FLOAT, index);
            output.setAtIndex(ValueLayout.JAVA_FLOAT, index, Math.max(0f, value));
        }
    }

    public void softmaxForward(MemorySegment input, MemorySegment output, int elementCount) {
        double max = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < elementCount; index++) {
            max = Math.max(max, input.getAtIndex(ValueLayout.JAVA_FLOAT, index));
        }

        double sum = 0d;
        for (int index = 0; index < elementCount; index++) {
            sum += Math.exp(input.getAtIndex(ValueLayout.JAVA_FLOAT, index) - max);
        }

        for (int index = 0; index < elementCount; index++) {
            double value = Math.exp(input.getAtIndex(ValueLayout.JAVA_FLOAT, index) - max) / sum;
            output.setAtIndex(ValueLayout.JAVA_FLOAT, index, (float) value);
        }
    }

    public void layernormForward(MemorySegment input, MemorySegment output, int elementCount, float epsilon) {
        double mean = 0d;
        for (int index = 0; index < elementCount; index++) {
            mean += input.getAtIndex(ValueLayout.JAVA_FLOAT, index);
        }
        mean /= elementCount;

        double variance = 0d;
        for (int index = 0; index < elementCount; index++) {
            double centered = input.getAtIndex(ValueLayout.JAVA_FLOAT, index) - mean;
            variance += centered * centered;
        }
        variance /= elementCount;

        double denominator = Math.sqrt(variance + epsilon);
        for (int index = 0; index < elementCount; index++) {
            double normalized = (input.getAtIndex(ValueLayout.JAVA_FLOAT, index) - mean) / denominator;
            output.setAtIndex(ValueLayout.JAVA_FLOAT, index, (float) normalized);
        }
    }
}
