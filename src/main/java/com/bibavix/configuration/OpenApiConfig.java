package com.bibavix.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do List API")
                        .version("1.0")
                        .description("API for managing tasks is a to-do list application"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic"))
                        .addSchemas("ErrorResponse", new Schema<Map<String, Object>>()
                                .type("object")
                                .addProperty("status", new Schema<Integer>().type("integer").example("400"))
                                .addProperty("error", new Schema<String>().type("string").example("Validation Failed"))
                                .addProperty("message", new Schema<Object>().example(Map.of("title", "Title is required")))));
    }
}
