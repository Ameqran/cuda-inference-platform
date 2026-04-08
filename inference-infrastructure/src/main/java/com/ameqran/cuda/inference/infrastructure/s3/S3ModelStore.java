package com.ameqran.cuda.inference.infrastructure.s3;

import com.ameqran.cuda.inference.application.port.ModelBinaryStore;
import com.ameqran.cuda.inference.domain.aggregate.Model;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

public class S3ModelStore implements ModelBinaryStore {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3ModelStore(S3Client s3Client, S3Presigner s3Presigner, String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadModel(Model model, byte[] rawModelBytes) {
        String key = "models/" + model.modelId().value() + ".onnx";
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/octet-stream")
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(rawModelBytes));
        return key;
    }

    @Override
    public String generatePresignedUrl(Model model) {
        String key = "models/" + model.modelId().value() + ".onnx";
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
