package com.microservices.apigateway.security;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@SpringBootApplication
public class ProductApplication {

    /**
     * This filter can be used to exclude specific metrics.
     * In the method below, we are hiding tomcat specific metrics.
     * @return
     */
    @Bean
    public MeterFilter excludeTomcatFilter() {
        return MeterFilter.denyNameStartsWith("tomcat");
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
