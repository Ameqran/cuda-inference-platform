package com.ameqran.cuda.inference.application.port;

import com.ameqran.cuda.inference.domain.layer.LayerGraph;

public interface ModelGraphParser {

    LayerGraph parse(byte[] onnxBytes);
}
