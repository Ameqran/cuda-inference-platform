package com.ameqran.cuda.inference.infrastructure.config;

import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import com.ameqran.cuda.inference.application.port.InputTensorValidator;
import com.ameqran.cuda.inference.application.port.ModelBinaryStore;
import com.ameqran.cuda.inference.application.port.ModelGraphParser;
import com.ameqran.cuda.inference.infrastructure.cuda.CublasContext;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaKernelLauncher;
import com.ameqran.cuda.inference.infrastructure.cuda.CudaRuntime;
import com.ameqran.cuda.inference.infrastructure.event.SpringDomainEventPublisher;
import com.ameqran.cuda.inference.infrastructure.onnx.LayerGraphBuilder;
import com.ameqran.cuda.inference.infrastructure.onnx.OnnxModelParser;
import com.ameqran.cuda.inference.infrastructure.onnx.WeightUploader;
import com.ameqran.cuda.inference.infrastructure.s3.S3ModelStore;
import com.ameqran.cuda.inference.infrastructure.validation.DefaultInputTensorValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public CudaRuntime cudaRuntime() {
        return new CudaRuntime();
    }

    @Bean
    public CublasContext cublasContext() {
        return new CublasContext();
    }

    @Bean
    public CudaKernelLauncher cudaKernelLauncher() {
        return new CudaKernelLauncher();
    }

    @Bean
    public WeightUploader weightUploader(CudaRuntime cudaRuntime) {
        return new WeightUploader(cudaRuntime);
    }

    @Bean
    public ModelGraphParser modelGraphParser() {
        return new OnnxModelParser(new LayerGraphBuilder());
    }

    @Bean
    public InputTensorValidator inputTensorValidator() {
        return new DefaultInputTensorValidator();
    }

    @Bean
    public DomainEventPublisher domainEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new SpringDomainEventPublisher(eventPublisher);
    }

    @Bean
    public S3Client s3Client(@Value("${platform.s3.endpoint:http://localhost:4566}") String endpoint,
                             @Value("${platform.s3.region:us-east-1}") String region,
                             @Value("${platform.s3.access-key:test}") String accessKey,
                             @Value("${platform.s3.secret-key:test}") String secretKey) {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(@Value("${platform.s3.endpoint:http://localhost:4566}") String endpoint,
                                   @Value("${platform.s3.region:us-east-1}") String region,
                                   @Value("${platform.s3.access-key:test}") String accessKey,
                                   @Value("${platform.s3.secret-key:test}") String secretKey) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public ModelBinaryStore modelBinaryStore(S3Client s3Client,
                                             S3Presigner s3Presigner,
                                             @Value("${platform.s3.bucket:model-store}") String bucket) {
        return new S3ModelStore(s3Client, s3Presigner, bucket);
    }
}
