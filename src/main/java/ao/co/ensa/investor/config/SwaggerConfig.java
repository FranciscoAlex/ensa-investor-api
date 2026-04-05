package ao.co.ensa.investor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ensaInvestorOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ENSA Investor Portal API")
                .description("REST API for the ENSA (Empresa Nacional de Seguros de Angola) Investor Platform. " +
                    "Provides authentication, investor management, and static data endpoints.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("ENSA Development Team")
                    .email("dev@ensa.co.ao")
                    .url("https://www.ensa.co.ao"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://www.ensa.co.ao/terms")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                        .description("Enter your JWT token")));
    }
}
