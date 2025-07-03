package com.ecomarket.gestion_logistica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API EcoMarket - Gestión Logística")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de gestión logística y envíos"));
    }
}
