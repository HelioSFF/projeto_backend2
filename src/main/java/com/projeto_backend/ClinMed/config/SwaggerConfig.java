package com.projeto_backend.ClinMed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ClinMed API")
                        .description("API REST para gerenciamento de clínica médica")
                        .version("1.0.0"));
    }
}
