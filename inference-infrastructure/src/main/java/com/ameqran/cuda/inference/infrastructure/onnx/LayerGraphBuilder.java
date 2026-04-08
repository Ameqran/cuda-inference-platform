package com.ameqran.cuda.inference.infrastructure.onnx;

import com.ameqran.cuda.inference.domain.layer.LayerGraph;
import com.ameqran.cuda.inference.domain.layer.LayerNode;

import java.util.List;

public class LayerGraphBuilder {

    public LayerGraph build(List<OnnxNodeDescriptor> nodes) {
        List<LayerNode> layerNodes = nodes.stream()
                .map(node -> new LayerNode(node.name(), node.opType(), node.inputs(), node.outputs()))
                .toList();

        return new LayerGraph(layerNodes);
    }
}
