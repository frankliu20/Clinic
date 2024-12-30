package by.gp.clinic.configuration;

import by.gp.clinic.service.TokenAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.GroupedOpenApi;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Jackson2ObjectMapperBuilder objectMapperBuilder;
    private final TokenAuthenticationService authenticationService;
    private final DataSource dataSource;

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
            .group("clinic")
            .pathsToMatch("/**")
            .addOpenApiCustomiser(openApi -> openApi
                .info(new Info().title("Clinic APIs").description("APIs for work with clinic server"))
                .addSecurityItem(new SecurityRequirement().addList("token")))
            .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("token", new SecurityScheme()
                    .type(Type.APIKEY)
                    .in(In.HEADER)
                    .name(TokenAuthenticationService.HEADER_STRING)))
            .info(new Info().title("Clinic APIs").description("APIs for work with clinic server"));
    }
}
