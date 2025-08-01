package com.example.tasksapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Task Management API")
                .version("v1")
                .description("Comprehensive REST API for task, project, team, and user management with advanced security and analytics.")
                .contact(new Contact().name("Your Team").email("support@example.com"))
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
            )
            .externalDocs(new ExternalDocumentation()
                .description("Full Documentation")
                .url("https://github.com/your-org/tasks-api-docs")
            );
    }
}
