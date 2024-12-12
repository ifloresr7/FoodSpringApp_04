package com.FoodSpringApp.FoodSpringApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Ejemplo")
                        .version("1.0")
                        .description("Documentación de la API con Springdoc OpenAPI")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
    }
}

