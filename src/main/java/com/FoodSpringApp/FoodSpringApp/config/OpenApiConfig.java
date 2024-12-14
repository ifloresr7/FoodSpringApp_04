package com.FoodSpringApp.FoodSpringApp.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Ejemplo")
                        .version("1.0.0")
                        .description("Documentación de la API de ejemplo con Springdoc OpenAPI")
                        .termsOfService("http://example.com/terms/")
                        .contact(new io.swagger.v3.oas.models.info.Contact().name("Soporte API").email("soporte@example.com"))
                        .license(new io.swagger.v3.oas.models.info.License().name("Licencia API").url("http://example.com/license")));
    }
}
