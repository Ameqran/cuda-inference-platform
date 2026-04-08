package com.ameqran.cuda.inference.infrastructure.onnx;

import java.util.List;

public record OnnxNodeDescriptor(String name, String opType, List<String> inputs, List<String> outputs) {
}
