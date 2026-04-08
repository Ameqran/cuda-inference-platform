package com.ameqran.cuda.inference.application.port;

import com.ameqran.cuda.inference.domain.aggregate.Model;

public interface ModelBinaryStore {

    String uploadModel(Model model, byte[] rawModelBytes);

    String generatePresignedUrl(Model model);
}
