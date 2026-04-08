package com.ameqran.cuda.inference.domain.layer;

import java.util.List;

public record LayerGraph(List<LayerNode> nodes) {

    public LayerGraph {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("LayerGraph must contain at least one node");
        }
        nodes = List.copyOf(nodes);
    }

    public int size() {
        return nodes.size();
    }
}
