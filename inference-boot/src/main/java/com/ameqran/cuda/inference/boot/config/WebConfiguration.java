package com.ameqran.cuda.inference.boot.config;

import com.ameqran.cuda.inference.boot.security.ApiKeyRateLimiterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public ApiKeyRateLimiterInterceptor apiKeyRateLimiterInterceptor() {
        return new ApiKeyRateLimiterInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyRateLimiterInterceptor()).addPathPatterns("/api/**");
    }
}
