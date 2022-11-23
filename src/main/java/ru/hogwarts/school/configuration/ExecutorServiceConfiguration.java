package ru.hogwarts.school.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfiguration {

    @Bean("threadA")
    public ExecutorService threadA() {
        return Executors.newFixedThreadPool(1);
    }

    @Bean("threadB")
    public ExecutorService threadB() {
        return Executors.newFixedThreadPool(1);
    }
}
