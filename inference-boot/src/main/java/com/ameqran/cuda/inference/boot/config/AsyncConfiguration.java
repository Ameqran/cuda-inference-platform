package com.ameqran.cuda.inference.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.VirtualThreadTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    public TaskExecutor gpuTaskExecutor() {
        return new VirtualThreadTaskExecutor("gpu-worker-");
    }
}
