package com.ameqran.cuda.inference.infrastructure.onnx;

import com.ameqran.cuda.inference.application.port.ModelGraphParser;
import com.ameqran.cuda.inference.domain.layer.LayerGraph;

import java.util.List;

public class OnnxModelParser implements ModelGraphParser {

    private final LayerGraphBuilder layerGraphBuilder;

    public OnnxModelParser(LayerGraphBuilder layerGraphBuilder) {
        this.layerGraphBuilder = layerGraphBuilder;
    }

    @Override
    public LayerGraph parse(byte[] onnxBytes) {
        if (onnxBytes == null || onnxBytes.length == 0) {
            throw new IllegalArgumentException("ONNX payload cannot be empty");
        }

        List<OnnxNodeDescriptor> nodes = List.of(
                new OnnxNodeDescriptor("input", "Input", List.of(), List.of("x")),
                new OnnxNodeDescriptor("linear_0", "Gemm", List.of("x"), List.of("y")),
                new OnnxNodeDescriptor("activation_0", "Relu", List.of("y"), List.of("z"))
        );

        return layerGraphBuilder.build(nodes);
    }
}
