package ru.hogwarts.avatar;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@OpenAPIDefinition
@SpringBootApplication(scanBasePackages = {
        "ru.hogwarts.avatar",
        "ru.hogwarts.shared"
})
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "ru.hogwarts.shared"
)
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
public class AvatarApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvatarApplication.class, args);
    }
}
