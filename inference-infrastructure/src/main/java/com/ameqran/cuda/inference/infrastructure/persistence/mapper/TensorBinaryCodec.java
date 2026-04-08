package com.ameqran.cuda.inference.infrastructure.persistence.mapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class TensorBinaryCodec {

    private TensorBinaryCodec() {
    }

    public static byte[] encode(float[] values) {
        ByteBuffer buffer = ByteBuffer.allocate(values.length * Float.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (float value : values) {
            buffer.putFloat(value);
        }
        return buffer.array();
    }

    public static float[] decode(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new float[0];
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        float[] values = new float[bytes.length / Float.BYTES];
        for (int index = 0; index < values.length; index++) {
            values[index] = buffer.getFloat();
        }
        return values;
    }
}
