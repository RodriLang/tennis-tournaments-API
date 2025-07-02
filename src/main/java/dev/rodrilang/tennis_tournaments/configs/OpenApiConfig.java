package dev.rodrilang.tennis_tournaments.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tennis Tournaments API",
                version = "1.0",
                description = "API para gestionar torneos de tenis.",
                contact = @Contact(
                        name = "Rodrigo Lang",
                        email = "rodrilangdev@gmail.com",
                        url = "https://github.com/RodriLang/tennis-tournaments-API"
                )
        )
)
public class OpenApiConfig {}
