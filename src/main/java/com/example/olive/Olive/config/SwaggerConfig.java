package com.example.olive.Olive.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Olive API",
                version = "1.0.0",
                description = "API documentation for Olive application",
                contact = @Contact(name = "Support Team", email = "support@olive.com"),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
        )
)
public class SwaggerConfig {
}

