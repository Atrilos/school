package ru.hogwarts.avatar;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@OpenAPIDefinition(
        info = @Info(title = "Avatar API", version = "1.0")
)
@SpringBootApplication(scanBasePackages = {
        "ru.hogwarts.avatar",
        "ru.hogwarts.shared"
})
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
